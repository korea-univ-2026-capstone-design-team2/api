#!/bin/sh
set -e

# ─────────────────────────────────────────────
# Config
# ─────────────────────────────────────────────
CONNECT_URL="http://debezium:8083"
CONNECTOR_NAME="psat-outbox-connector"
MAX_WAIT_SECONDS=120
STATUS_CHECK_RETRIES=10
STATUS_CHECK_INTERVAL=3

mask_secret() {
  sed -E 's/"database\.password" *: *"[^"]*"/"database.password": "***"/'
}

# ─────────────────────────────────────────────
# 1. Kafka Connect REST API가 응답할 때까지 대기
# ─────────────────────────────────────────────
echo "Waiting for Kafka Connect..."

ELAPSED=0
until curl -sf "${CONNECT_URL}/connectors" > /dev/null 2>&1; do
  if [ "$ELAPSED" -ge "$MAX_WAIT_SECONDS" ]; then
    echo "ERROR: Kafka Connect did not become ready within ${MAX_WAIT_SECONDS}s"
    exit 1
  fi
  echo "  still waiting... (${ELAPSED}s elapsed)"
  sleep 3
  ELAPSED=$((ELAPSED + 3))
done

echo "Kafka Connect is ready."

# ─────────────────────────────────────────────
# 2. 설정 파일 생성
# ─────────────────────────────────────────────
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

envsubst \
  '$DB_HOST $DB_PORT $DB_NAME $DEBEZIUM_DB_USER $DEBEZIUM_DB_PASSWORD $KAFKA_BOOTSTRAP_SERVERS' \
  < "${SCRIPT_DIR}/connector.json.template" \
  > /tmp/connector.json

echo "Generated config (secrets masked):"
mask_secret < /tmp/connector.json

jq '.config' /tmp/connector.json > /tmp/connector-config-only.json

# ─────────────────────────────────────────────
# 3. 기존 커넥터 존재 여부 확인 (등록 vs 갱신 분기)
# ─────────────────────────────────────────────
EXISTING_STATUS_CODE=$(curl -s -o /dev/null -w "%{http_code}" \
  "${CONNECT_URL}/connectors/${CONNECTOR_NAME}/status" || echo "000")

if [ "$EXISTING_STATUS_CODE" = "200" ]; then
  echo "Connector '${CONNECTOR_NAME}' already exists. Updating config..."

  HTTP_CODE=$(curl -s -o /tmp/register-response.json -w "%{http_code}" \
    -X PUT \
    -H "Content-Type: application/json" \
    --data @/tmp/connector-config-only.json \
    "${CONNECT_URL}/connectors/${CONNECTOR_NAME}/config")

  EXPECTED_CODES="200 201"
else
  echo "Registering new connector '${CONNECTOR_NAME}'..."

  HTTP_CODE=$(curl -s -o /tmp/register-response.json -w "%{http_code}" \
    -X POST \
    -H "Content-Type: application/json" \
    --data @/tmp/connector.json \
    "${CONNECT_URL}/connectors")

  EXPECTED_CODES="200 201"
fi

echo "Response (HTTP ${HTTP_CODE}):"
mask_secret < /tmp/register-response.json
echo ""

CODE_OK=0
for code in $EXPECTED_CODES; do
  if [ "$HTTP_CODE" = "$code" ]; then
    CODE_OK=1
  fi
done

if [ "$CODE_OK" -ne 1 ]; then
  echo "ERROR: Connector registration/update failed with HTTP ${HTTP_CODE}"
  exit 1
fi

# ─────────────────────────────────────────────
# 4. 커넥터가 실제로 RUNNING 상태인지 확인
# ─────────────────────────────────────────────
echo "Verifying connector state..."

RETRY=0
STATE=""

while [ "$RETRY" -lt "$STATUS_CHECK_RETRIES" ]; do
  STATUS_JSON=$(curl -sf "${CONNECT_URL}/connectors/${CONNECTOR_NAME}/status" || true)

  STATE=$(printf '%s' "$STATUS_JSON" \
    | grep -o '"state":"[A-Z]*"' \
    | head -1 \
    | cut -d'"' -f4)

  if [ "$STATE" = "RUNNING" ]; then
    break
  fi

  RETRY=$((RETRY + 1))
  echo "  connector state=${STATE:-UNKNOWN}, retrying (${RETRY}/${STATUS_CHECK_RETRIES})..."
  sleep "$STATUS_CHECK_INTERVAL"
done

echo ""
echo "Final connector status:"
mask_secret < /dev/null
curl -sf "${CONNECT_URL}/connectors/${CONNECTOR_NAME}/status"
echo ""

if [ "$STATE" != "RUNNING" ]; then
  echo "ERROR: Connector '${CONNECTOR_NAME}' is not RUNNING (last observed state=${STATE:-UNKNOWN})"
  exit 1
fi

TASK_FAILED=$(printf '%s' "$STATUS_JSON" | grep -o '"tasks":\[.*"state":"FAILED"' || true)
if [ -n "$TASK_FAILED" ]; then
  echo "ERROR: Connector is RUNNING but at least one task has FAILED"
  exit 1
fi

echo "Connector registration completed successfully (state=RUNNING)."

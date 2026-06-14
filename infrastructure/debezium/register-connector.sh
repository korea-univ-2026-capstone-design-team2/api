#!/bin/sh

set -e

CONNECT_URL="http://debezium:8083"
CONNECTOR_NAME="psat-outbox-connector"

echo "Waiting for Kafka Connect..."

until curl -sf "${CONNECT_URL}/connectors" > /dev/null; do
  echo "  still waiting..."
  sleep 3
done

echo "Kafka Connect is ready."

EXISTING=$(curl -sf "${CONNECT_URL}/connectors/${CONNECTOR_NAME}/status" || true)

if [ -n "$EXISTING" ]; then
  echo "Connector already registered. Skipping."
  exit 0
fi

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

echo "Registering Debezium Outbox Connector..."

envsubst \
'$DB_HOST $DB_PORT $DB_NAME $DEBEZIUM_DB_USER $DEBEZIUM_DB_PASSWORD $KAFKA_BOOTSTRAP_SERVERS' \
< "${SCRIPT_DIR}/connector.json.template" \
| curl \
    -X POST \
    -H "Content-Type: application/json" \
    --data @- \
    "${CONNECT_URL}/connectors"

echo ""
echo "Waiting for connector registration..."

sleep 2

echo ""
echo "Connector status:"

curl -sf "${CONNECT_URL}/connectors/${CONNECTOR_NAME}/status"

echo ""
echo "Connector registration completed."

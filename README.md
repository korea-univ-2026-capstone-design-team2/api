# 🧠 PSAT 언어논리 AI 문제생성 백엔드

> AI 기반 5급 공무원 공개경쟁채용시험 (PSAT) 언어논리 영역 문제 자동 생성 서비스의 백엔드 레포지토리입니다.  
> 10년치 기출 DB에서 **논리적 프레임(Logical Frame)** 을 추출하고, 신규 주제를 주입하여 고품질 모의고사 문제를 생성합니다.

---

## 📌 목차

- [서비스 개요](#서비스-개요)
- [기술 스택](#기술-스택)
- [로컬 환경 설정](#로컬-환경-설정)
- [빌드 및 실행](#빌드-및-실행)
- [커밋 컨벤션](#커밋-컨벤션)
- [브랜치 전략](#브랜치-전략)
- [API 문서](#api-문서)

---

## 서비스 개요

### 핵심 전략: RAG 기반 문제 생성

```
[기출 DB (10년치)]
       │
       ▼
 논리 프레임 추출
 (Logical Frame)
       │
       ▼
  Vector DB 저장
       │
  신규 주제 입력
       │
       ▼
유사 프레임 검색 (RAG)
       │
       ▼
  LLM 프롬프트 실행
       │
       ▼
  신규 문제 생성 ✅
```

- **RAG (Retrieval-Augmented Generation)**: 기출 문제의 구조·논리 패턴을 벡터화하여 유사 프레임을 검색, LLM에 컨텍스트로 주입
- **논리 프레임**: 문제의 발문 구조, 선택지 구성 패턴, 난이도 특성 등 추상화된 메타 정보
- **품질 보장**: 실제 PSAT 출제 경향을 학습한 데이터 기반으로 문체와 논리 구조를 유지

---

## 기술 스택

| 구분 | 기술 |
|------|------|
| Language | Java 25 |
| Framework | Spring Boot 4.x.x |
| Build | Gradle  |
| Database | MySQL 8.x |
| Vector DB | pgvector / Qdrant (TBD) |
| AI | Anthropic Claude API / OpenAI API |
| Infra | Docker, Docker Compose |

> **Virtual Threads**: AI API 호출 및 DB I/O 등 블로킹 작업에 Project Loom의 가상 스레드를 활용하여 처리량을 극대화합니다.

---

## 로컬 환경 설정

### Prerequisites

- Java 25+
- Docker & Docker Compose
- Gradle 9.x

### 인프라 실행 (Docker Compose)

```bash
docker compose -f docker/docker-compose.dev.yml up -d
```

실행되는 서비스: 메인 서버, MySQL 8, Vector DB

### 환경변수 설정

```bash
cp .env.example .env
# .env 파일을 열어 필요한 값 입력 (아래 환경변수 섹션 참고)
```

---

## 빌드

```bash
# 전체 빌드
./gradlew build

# 테스트 제외 빌드
./gradlew build -x test
```

---

## 커밋 컨벤션

### 형식

```
<type>(<scope>): <subject>

[optional body]

[optional footer]
```

### Type 목록

| Type | 설명 |
|------|------|
| `feat` | 새로운 기능 추가 |
| `fix` | 버그 수정 |
| `docs` | 문서 수정 (README, 주석 등) |
| `style` | 코드 포맷팅, 세미콜론 누락 등 (로직 변경 없음) |
| `refactor` | 리팩토링 (기능 변경 없음) |
| `test` | 테스트 코드 추가 및 수정 |
| `chore` | 빌드 설정, 의존성 업데이트 등 |
| `perf` | 성능 개선 |
| `ci` | CI/CD 설정 변경 |

### Scope (선택)

모듈 또는 도메인 단위로 작성합니다.  
예: `question`, `auth`, `shared`, `member`, `bootstrap`

### 예시

```bash
feat(question): 기출 논리 프레임 벡터화 저장 기능 추가

RAG 파이프라인의 첫 단계로, 기출 문제에서 추출한 논리 프레임을
Vector DB에 저장하는 Port와 Adapter를 구현합니다.

Resolves: #42
```

```bash
fix(ai-client): 토큰 초과 시 프롬프트 잘림 오류 수정
refactor(question): QuestionGenerationService 메서드 분리
docs: 로컬 환경 설정 가이드 업데이트
```

---

## 브랜치 전략

`main` → `develop` → `feature/*` / `fix/*` / `refactor/*`

| 브랜치               | 설명 |
|-------------------|------|
| `main`            | 프로덕션 배포 브랜치. 직접 푸시 금지 |
| `develop`         | 통합 개발 브랜치. PR을 통해서만 머지 |
| `feature/{scope}` | 기능 개발 브랜치 |
| `fix/{scope}`      | 버그 수정 브랜치 |
| `refactor/{scope}` | 리팩토링 브랜치 |

```bash
# 예시
git checkout -b feature/auth
git checkout -b fix/question
```

---

## API 문서

로컬 서버 실행 후 아래 URL에서 Swagger UI를 확인할 수 있습니다.

```
http://localhost:8080/swagger-ui.html
```

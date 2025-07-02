# MetaBlock CMS & Spring Boot Demo

Payload CMS **v3**(Next.js 기반)와 Spring Boot **3.2** 애플리케이션을 결합한 풀스택 데모 프로젝트입니다.  
• `cms` 디렉터리: Payload CMS + Next.js 프런트엔드 (포트 `3001`)  
• `src` 디렉터리: Spring Boot 서버 + Thymeleaf 뷰 (포트 `8080`)  

## 폴더 구조

| 경로 | 설명 |
|------|------|
| `cms/` | Payload CMS 및 Next.js 소스, Dockerfile, 테스트 코드 등 |
| `src/main/java` | Spring Boot 백엔드 소스 코드 |
| `src/main/resources/templates` | Thymeleaf 템플릿(웹 사이트 뷰) |
| `.mvn/`, `mvnw*` | Maven Wrapper (Maven 3.9.9) |

## 요구 사항

* **Java 17** 이상 (Homebrew 설치 예시)
  ```bash
  brew install openjdk@17
  export JAVA_HOME="/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
  export PATH="$JAVA_HOME/bin:$PATH"
  ```
* **Node.js 18 LTS**
* **Docker**(선택) ‑ CMS용 `docker-compose.yml` 사용 시 필요

## 빠른 시작

### 1. Payload CMS(포트 3001)

#### 로컬 실행

```bash
cd cms
npm install      # 최초 1회
npm run dev      # http://localhost:3001
```

관리자 대시보드: <http://localhost:3001/admin>

#### Docker 실행 (선택)

```bash
cd cms
docker-compose up -d
```

### 2. Spring Boot 사이트(포트 8080)

루트 디렉터리에서:

```bash
./mvnw clean spring-boot:run
# 또는
mvn spring-boot:run
```

접속 URL 예시

* 웹 홈: <http://localhost:8080/cms/home>
* 포스트 상세: <http://localhost:8080/cms/post/{id}>

## 주요 기능

* **Headless CMS**: Payload CMS에서 작성한 콘텐츠를 REST API로 제공
* **HTML 렌더링**: Spring Boot가 Rich-Text(Lexical) JSON을 HTML로 변환하여 출력
* **Thymeleaf 템플릿**: 부트스트랩 UI 구성, 서버사이드 렌더링
* **Maven Wrapper**: `.mvn/jvm.config` 로 Java 17 고정

## 배포

```bash
git clone https://github.com/your-id/metablock-web.git
cd metablock-web
./mvnw package      # `target/metablock-web-*.jar` 생성
java -jar target/metablock-web-*.jar
```

## 라이선스

MIT License (see `LICENSE`).

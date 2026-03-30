# APTeN - Backend

## 📌 프로젝트 소개

APTeN은 입주민과 관리자가 함께 사용하는 스마트 아파트 통합 관리 시스템입니다.  
방문차량 등록부터 주차 현황, 시설 예약, 게시판까지 아파트 생활 전반을 한 곳에서 관리할 수 있습니다.

![Image](https://github.com/user-attachments/assets/4f37a3ca-2ea4-4ae1-82df-44307f5ec994) ![Image](https://github.com/user-attachments/assets/6d238e3a-862e-4fec-9414-c191b4d2b05e)

### 주요 기능
- 방문차량 사전등록 및 고정 방문차량 관리
- 입출차 기록 생성 (번호판 입력 시 차량 유형 자동 판별) 및 실시간 주차 현황 조회
- 시설 예약 및 GX 프로그램 승인 관리
- 게시판 (공지사항 / 자유게시판) 및 댓글
- 관리자 알림 (신규 회원가입, 차량 등록 신청 시 자동 발송)

### 💡 특징
- 번호판 입력만으로 등록차량 → 방문차량 → 고정방문차량 → 미등록차량 순서로 자동 판별하여 입출차 기록 생성
- 단순 IN/OUT 카운트 차이가 아닌 NOT EXISTS 서브쿼리 기반으로 실시간 주차 현황을 정확하게 계산
- 관리자 이벤트 발생 시 DB 기반 알림 자동 저장 구조 구현 (폴링 방식)
- JWT를 HttpOnly 쿠키로 관리해 XSS 공격 방어 및 자동 재발급 인터셉터 적용

## 🏗️ 아키텍처
```
Frontend (Vue.js) ↔ Backend (Spring Boot) ↔ Database (MySQL)
```

Vite 빌드 결과물을 Spring Boot `resources/static/`에 포함하여 단일 서버로 서빙합니다.

## 🛠 기술 스택

- Java 17, Spring Boot 3.x
- MyBatis, MySQL 8.0
- Spring Security + JWT (HttpOnly 쿠키)
- OAuth2 소셜 로그인 (Google, Kakao, Naver)
- Gradle

## 📁 프로젝트 구조
```
src/main/java/com/apt/
├── common/          # 공통 응답, 예외처리
├── config/          # Security, JWT, OAuth2 설정
├── auth/            # 로그인, 회원가입, 소셜 로그인
├── user/            # 내 정보 조회/수정
├── household/       # 세대 관리
├── infra/mail/      # 비밀번호 재설정 메일
├── notification/    # 관리자 알림
├── board/           # 게시판, 댓글
├── visitorvehicle/  # 방문차량, 고정 방문차량
├── parking/         # 입출차 기록, 주차 현황/통계
├── vehicle/         # 입주민 차량
├── facility/        # 시설 관리
└── reservation/     # 예약
```

## ⚙️ 실행 방법

프로젝트 루트에 `.env` 파일을 생성하고 아래 항목을 설정해 주세요.
```env
# Database
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/my_apten
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=your_password
SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver

# Server
SERVER_PORT=8080

# JWT
CONSTANTS_JWT_ISSUER=apten
CONSTANTS_JWT_BEARER_FORMAT=Bearer
CONSTANTS_JWT_CLAIM_KEY=user
CONSTANTS_JWT_SECRET_KEY=your_secret_key
CONSTANTS_JWT_ACCESS_TOKEN_COOKIE_NAME=access_token
CONSTANTS_JWT_ACCESS_TOKEN_COOKIE_PATH=/
CONSTANTS_JWT_ACCESS_TOKEN_COOKIE_VALIDITY_SECONDS=3600
CONSTANTS_JWT_ACCESS_TOKEN_VALIDITY_MILLISECONDS=3600000
CONSTANTS_JWT_REFRESH_TOKEN_COOKIE_NAME=refresh_token
CONSTANTS_JWT_REFRESH_TOKEN_COOKIE_PATH=/
CONSTANTS_JWT_REFRESH_TOKEN_COOKIE_VALIDITY_SECONDS=604800
CONSTANTS_JWT_REFRESH_TOKEN_VALIDITY_MILLISECONDS=604800000

# OAuth2 - Google
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID=your_google_client_id
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET=your_google_client_secret

# OAuth2 - Kakao
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_KAKAO_CLIENT_ID=your_kakao_client_id
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_KAKAO_CLIENT_SECRET=your_kakao_client_secret

# OAuth2 - Naver
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_NAVER_CLIENT_ID=your_naver_client_id
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_NAVER_CLIENT_SECRET=your_naver_client_secret

# Mail
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=your_email@gmail.com
SPRING_MAIL_PASSWORD=your_app_password
SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH=true
SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE=true

# File Upload
FILE_DIRECTORY=/your/upload/path

# App Base URL (비밀번호 재설정 링크용)
APP_BASE_URL=http://localhost:5173
```

DB를 먼저 생성한 후 실행해 주세요.
```bash
# DB 생성
CREATE DATABASE my_apten DEFAULT CHARACTER SET utf8mb4;

# 실행
./gradlew bootRun
```

## ⚠️ 배포 환경 주의사항

로컬 환경에서는 모든 기능이 정상 동작하지만,  
HTTP 기반 IP 주소로 배포 시 아래 기능은 동작하지 않습니다.

| 기능 | 원인 | 해결 방법 |
|---|---|---|
| 소셜 로그인 (Google/Kakao/Naver) | OAuth2는 HTTPS + 도메인 필수 | HTTPS 도메인 적용 후 각 콘솔에서 redirect URI 등록 |
| 비밀번호 재설정 | 메일 링크가 `APP_BASE_URL` 기준으로 생성됨 | `.env`의 `APP_BASE_URL`을 배포 서버 주소로 변경 |

## 👥 팀원

| 이름 | 담당 |
|---|---|
| 김가은 | 인증, 소셜 로그인, 방문차량, 고정 방문차량, 입출차, 주차, 알림 |
| 이윤주 | 입주민 대시보드, 게시판, 댓글 |
| 손지혜 | 관리자 대시보드, 차량, 시설 |
| 박소영 | 세대 관리, 예약 |

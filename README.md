# ⚓ DATT. (Datt: Record Your Anchor)

<img width="1408" height="768" alt="Image" src="https://github.com/user-attachments/assets/d3327e5e-3586-401b-8e42-b6291cc7268d" />

**"당신이 머문 공간을 닻(Anchor)으로 기록하세요."**
> DATT는 사용자가 방문한 장소에 대한 기억을 기록하고 공유하는 위치 기반 기록 서비스입니다.

## 🚀 서비스 핵심 기능
- **위치 기반 장소 검색**: 네이버/구글 맵 크롤링 데이터를 기반으로 정확한 장소 정보를 제공합니다.
- **닻(Anchor) 생성**: 특정 장소에 나만의 기록(닻)을 남겨 추억을 보관합니다.

## 🛠 기술 스택

### **Frontend**
- **Framework**: Next.js 14+ (App Router)
- **Language**: TypeScript
- **Styling**: Tailwind CSS
- **State Management**: React Hooks (useState, useEffect, useMemo)

### **Backend**
- **Framework**: Spring Boot 3.x / Java 17
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA / Hibernate Spatial
- **Architecture**: Domain-Driven Design (DDD) 구조 지향
- **API**: RESTful API 설계

### **Data Agent**
- **Language**: Python
- **Framework**: FastAPI
- **Library**: Playwright (Web Scraping)

## 🏗 시스템 아키텍처
서비스는 유연한 확장을 위해 **프론트엔드, 백엔드, 데이터 수집 에이전트** 세 부분으로 분리되어 독립적으로 동작합니다.

- **Frontend**: 사용자 인터페이스 제공 및 클라이언트 사이드 데이터 인터랙션
- **Backend**: 비즈니스 로직 처리 및 장소/기록 데이터 관리
- **Agent**: 실시간 장소 데이터 크롤링 및 데이터 가공/제공
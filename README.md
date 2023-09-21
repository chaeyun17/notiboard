## 기술 목록

- Language: **Java 17**
- Frameworks: **Spring Boot 3, Hibernate**
- Build Tool: **Gradle 8**
- DB: **MySQL, Redis**
- Test Tools: **Rest assured, jUnit 5, AssertJ, TestContainers(MySQL)**
- VCS: **Git, Github**

## 실행 환경 구성

```shell
# The Multiple Runtime Version Manager: ASDF
$ brew install asdf
$ asdf plugin-add gradle https://github.com/rfrancis/asdf-gradle.git
$ asdf plugin-add java https://github.com/halcyon/asdf-java.git
$ asdf install
```

```shell
# 실행
./gradlew bootRun
```

## 코드 스타일 컨벤션

- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- `intellij-java-google-style.xml` Import

--- 

## 기능 상세 정의

- 공지사항을 등록할 수 있어야 한다.
    - 요청
        - 제목, 내용, 공지 시작일시, 공지 종료일시, 첨부파일 (여러개)
    - 유효성 체크
        - 필수 입력값: 제목, 내용, 공지시작일시, 공지종료일시
        - 옵션 입력값: 첨부파일
        - 제목은 100자 이하
        - 공지 시작일시와 종료일시는 시간 포맷 ISO UTC
        - 공지 시작일시는 종료일시보다 늦으면 안된다.
        - 공지 종료일시는 시작일시보다 빠르면 안된다.
        - 첨부파일은 2MB 이하여야 한다.
        - 첨부파일 0 bytes 는 안된다.
    - 응답
        - `201 Created`
- 공지사항 삭제
    - 요청
        - ID
    - Soft Delete
    - 유효성 체크
        - 공지사항 존재 여부
    - 응답
        - `204 (No content)`
- 공지사항 수정
    - 요청
        - 제목, 내용, 공지 시작일시, 공지 종료일시, 첨부파일 (여러개)
        - 새 첨부파일 추가
        - 기존 첨부파일 제거
    - 유효성 체크
        - 생성과 동일한 유효성 체크
        - 공지사항 존재 여부
    - 응답
        - `200 (OK)`
- 공지사항 단일 조회
    - 요청
        - ID
    - 응답
        - 제목, 내용, 공지 시작일시, 공지 종료일시, 첨부파일 다운로드 링크들
        - `200 (OK)`
        - `HTTP 404 (Not found)`
- 공지사항 목록 조회
    - 요청
        - 파라미터: 페이지 순번, 페이지네이션 숫자
        - 정렬: 제목, 내용, 공지시작일시, 공지 종료일시 오름차순과 내림차순
        - 검색: 제목 또는 내용 포함 검색, 기간 검색
    - 응답
        - 제목, 내용, 공지 시작일시, 공지 종료일시, 첨부파일 다운로드 링크들
- 응답
    - 유효성 체크 실패 `400 Bad Request`
    - 존재 하지 않는 리소스에 대한 응답 `404 Not Found`
    - 서버 에러 `500 Internal Server`
    - 응답 Body 내 에러코드와 에러 원인 메시지

## 도메인 설계

- Notice 공지사항
    - 공지사항 생성
    - 공지사항 삭제
    - 공지사항 수정
    - 공지사항 단일 조회
    - 공지사항 검색
    - 유효성 체크
        - 필수 입력값: 제목, 내용, 공지시작일시, 공지종료일시
        - 옵션 입력값: 첨부파일
        - 제목은 100자 이하
        - 공지 시작일시와 종료일시는 시간 포맷 ISO 8601
        - 공지 시작일시는 종료일시보다 늦으면 안된다.
        - 첨부파일은 2MB 이하여야 한다.
- Title 제목
    - 제목 생성
        - 처음과 끝에 공란은 없앤다.
    - 제목 수정
        - 처음과 끝에 공란은 없앤다.
    - 유효성 체크
        - 1자~100자
- 내용 Content
    - 생성
    - 수정
    - 유효성 체크
        - 1자~1000자
- Attachment 첨부파일
    - 첨부파일 생성
    - 첨부파일 삭제
    - 첨부파일 다운로드 링크 생성
    - 유효성 체크
        - 용량은 2MB 이하여야 한다.
- FileStorage Interface
    - 파일을 저장한다
    - 파일을 불러온다.
    - 파일을 삭제한다.
- LocalStorage
    - 파일을 디스크에 저장한다.
    - 파일을 디스크에 불러온다.
    - 파일을 디스크에서 삭제한다.
        - 존재하지 않는 파일이면, Not Found
- Period 기간
    - 시작일시와 종료일시로 생성
    - 시작일시와 종료일시로 수정
    - 입출력 시간 포맷은 ISO 8601
    - 유효성 체크
        - 시작일시와 종료일시는 같을 수 없다.
        - 시작일시는 종료일시보다 빨라야 한다.
        - 종료일시는 시작일시보다 늦어야 한다.
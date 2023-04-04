## Title: [1Week] 김경렬

### 미션 요구사항 분석 & 체크리스트

---

매 주 제공되는 미션 별 요구사항을 기반으로 기능에 대한 분석을 진행한 후, 아래와 같은 체크리스트를 작성합니다.

- ‘어떻게 개발을 진행 할 것인지에 대한 방향성’을 확인하는 과정이기 때문에 최대한 깊이있게 분석 후 진행해주시기 바랍니다.


### 1주차 미션 요약

---

**[접근 방법]**

**1) 호감 삭제를 구현 시, JPA의 delete()가 작동이 안되는 문제 발생(SQL문 자체가 생성이 안됨)**
  1. deleteById()는 되는지 확인 &rarr; 똑같이 무반응(SQL문 자체가 생성X)
  2. 디버깅으로 삭제할 데이터를 제대로 찾아왔는지 확인 &rarr; 제대로 찾음
  3. JPQL로 delete()를 만들면 작동이 되는지 확인 &rarr; 작동됨!
  4. 하지만 아직도 기본 내장 delete()는 작동 하지 않음
  5. 마지막으로 트랜잭션 문제인지 확인
  > **해결 방법**<br><br>서비스 클래스에 @Transactional(readOnly = true) 속성을 false로 바꿔주니 해결됨.<br>읽기 전용 모드였기에 수정/삭제 같은 작업이 안되었던 것<br><br>그렇다면 왜 JPQL로 했을 땐 실행이 된 것인가?<br>&rarr; @Query를 달면 영속성 컨테이너를 거치지 않고, 직접 데이터베이스로 질의작성을 하기 때문이다!<br><br> 만약 JPQL로 삭제를 구현한다면, 영속성 컨테이너와 데이터베이스 간의 데이터 일치성에 문제가 생길 수 있다. 그러므로 삭제하기 전, 영속성 컨테이너에서 먼저 조회 후 삭제를 진행해야한다!<br>이 부분은 영속성 컨테이너와 EntityManager를 더욱 자세히 공부해야겠다.

<br>

**2) 구글 소셜 로그인 구현 후, 400 오류: redirect_uri_mismatch 발생**
  1. [Google Developers 공식 문서](https://developers.google.com/identity/protocols/oauth2/web-server?hl=ko#authorization-errors-redirect-uri-mismatch) `redirect_uri_mismatch` 파트 참조 &rarr; 승인 요청에 전달된 redirect_uri가 OAuth 클라이언트 ID의 승인된 리디렉션 URI와 일치하지 않는다. 라는 답변을 확인함!
  2. 구글 클라우드 플랫폼 **승인된 리디렉션 URI** 설정을 다시 확인함
  > **해결 방법**<br><br>구글 클라우드 플랫폼에서 기존에는 승인된 리디렉션 URI를 `http://localhost:8080/oauth2/authorization/google` 로 추가를 했었는데, 카카오 로그인 설정에도 `http://localhost:8080/oauth2/authorization/kakao` 라는 URI는 사용하지 않았었다! 그래서 구글 클라우드 설정에서 리디렉션 URI를 `http://localhost:8080/login/oauth2/code/google` 로 고쳐주니 정상 작동됐다.

**3) 네이버 소셜 로그인 구현 후, 상단 id값 출력하는 부분 다른 소셜 로그인 형식과 다름**
  1. 상단 id값 출력하는 부분이 `NAVER__{id=G2U4UwpPElaNBnGLBG9TvNWYjXJ5nA3cg9UNufVnxb0, gender=M, name=김경렬}` 형태로 나옴
  2. 뒤에 `gender`, `name`이 출력? &rarr; 네이버 디벨로퍼에서 사용 API 점검 &rarr; 필수 항목에 `회원이름` `성별` 선택해제
  3. `gender`와 `name`은 없어졌지만, 아직 JSON 형태로 출력되고 너무 길어서 마음에 안듬
  > **해결 방법**<br><br>완벽한 해결법을 찾진 못했지만, 우선 필수 항목에 `회원이름`을 추가해서 너무 길었던 `고유 id값` 대신 이름을 출력하게 했다.<br>ex) NAVER__김경렬<br><br>***새로운 문제 발견***<br>DB MEMBER 테이블에 username이 `NAVER__김경렬`과 같이 저장되어서 동명이인이 네이버 로그인으로 가입할 경우, username 컬럼은 UNIQUE 속성이기에 오류가 날 수 있다!<br><br>&rarr; 임시 고유 값을 추가해주거나 길어도 원래의 id를 출력하게 해야하나..? (해결 안됨)

**[특이사항]**

구현 과정에서 아쉬웠던 점 / 궁금했던 점을 정리합니다.

- 추후 리팩토링 시, 어떤 부분을 추가적으로 진행하고 싶은지에 대해 구체적으로 작성해주시기 바랍니다.

  **참고: [Refactoring]**

    - Refactoring 시 주로 다루어야 할 이슈들에 대해 리스팅합니다.
    - 1차 리팩토링은 기능 개발을 종료한 후, 스스로 코드를 다시 천천히 읽어보면서 진행합니다.
    - 2차 리팩토링은 피어리뷰를 통해 전달받은 다양한 의견과 피드백을 조율하여 진행합니다.
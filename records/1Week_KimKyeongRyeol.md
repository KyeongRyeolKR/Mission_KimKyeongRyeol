## Title: [1Week] 김경렬

### 미션 요구사항 분석 & 체크리스트

---

- [x] 호감상대 삭제
  ##### [요구사항]
  1. 호감목록 페이지에서 특정 항목의 삭제 버튼을 누르면 삭제되어야 한다.
  2. 삭제를 처리하기 전, 해당 항목에 대한 소유권을 체크해야 한다.
  3. 삭제 후, 호감목록 페이지로 돌아와야 한다.
  
  <br><br>

- [x] 구글 로그인 연동
  ##### [요구사항]
  1. 로그인 페이지에서 구글 로그인 버튼을 누르면 가입 및 로그인 처리가 되어야 한다.
  2. 최초 로그인 시, 가입이 되고 그 이후부터는 가입은 되지 않는다.


### 1주차 미션 요약

---

**[접근 방법]**

**1) 호감 삭제를 구현 시, JPA의 delete()가 작동이 안되는 문제 발생(SQL문 자체가 생성이 안됨)**
  1. deleteById()는 되는지 확인 &rarr; 똑같이 무반응(SQL문 자체가 생성X)
  2. 디버깅으로 삭제할 데이터를 제대로 찾아왔는지 확인 &rarr; 제대로 찾음
  3. JPQL로 delete()를 만들면 작동이 되는지 확인 &rarr; 작동됨!
  4. 하지만 아직도 기본 내장 delete()는 작동 하지 않음
  5. 마지막으로 트랜잭션 문제인지 확인
  > **해결 방법**<br><br>
  맨위에 @Transactional(readOnly = true)를 보지 못해서 생긴 오류였다...<br><br>
  서비스 클래스에 @Transactional(readOnly = true) 속성을 false로 바꿔주니 해결됨.<br>
  읽기 전용 모드였기에 수정/삭제 같은 작업이 안되었던 것<br><br>
  **2차 수정** &rarr; 일반적으로 서비스 클래스에는 @Transactional(readOnly = true)를 달아주고, SELECT 이외의 쿼리문을 실행할 가능성이 있는 메소드는 @Transactional을 따로 붙여준다고 한다.<br>
  이유 : readOnly = true 옵션을 주면, 강제로 flush 하지 않는 이상 영속성 컨테이너와 DB간 동기화(flush)가 일어나지 않아 변경 감지를 위한 스냅샷을 보관하지 않는다. 그렇기에 성능이 향상될 수 있다.<br><br>
  그렇다면 왜 JPQL로 했을 땐 실행이 된 것인가?<br>
  &rarr; @Query를 달면 영속성 컨테이너를 거치지 않고, 직접 데이터베이스로 질의작성을 하기 때문이다!<br><br> 
  만약 JPQL로 삭제를 구현한다면, 영속성 컨테이너와 데이터베이스 간의 데이터 일치성에 문제가 생길 수 있다. 그러므로 삭제하기 전, 영속성 컨테이너에서 먼저 조회 후 삭제를 진행해야한다!<br><br>
  이 부분은 영속성 컨테이너와 EntityManager를 더욱 자세히 공부해야겠다.

<br>

**2) 구글 소셜 로그인 구현 후, 400 오류: redirect_uri_mismatch 발생**
  1. [Google Developers 공식 문서](https://developers.google.com/identity/protocols/oauth2/web-server?hl=ko#authorization-errors-redirect-uri-mismatch) `redirect_uri_mismatch` 파트 참조 &rarr; 승인 요청에 전달된 redirect_uri가 OAuth 클라이언트 ID의 승인된 리디렉션 URI와 일치하지 않는다. 라는 답변을 확인함!
  2. 구글 클라우드 플랫폼 **승인된 리디렉션 URI** 설정을 다시 확인함
  > **해결 방법**<br><br>
  구글 클라우드 플랫폼에서 기존에는 승인된 리디렉션 URI를 `http://localhost:8080/oauth2/authorization/google` 로 추가를 했었는데, 카카오 로그인 설정에도 `http://localhost:8080/oauth2/authorization/kakao` 라는 URI는 사용하지 않았었다! 그래서 구글 클라우드 설정에서 리디렉션 URI를 `http://localhost:8080/login/oauth2/code/google` 로 고쳐주니 정상 작동됐다.

<br>

**3) 네이버 소셜 로그인 구현 후, 상단 id값 출력하는 부분 다른 소셜 로그인 형식과 다름**
  1. 상단 id값 출력하는 부분이 `NAVER__{id=G2U4UwpPElaNBnGLBG9TvNWYjXJ5nA3cg9UNufVnxb0, gender=M, name=김경렬}` 형태로 나옴
  2. 뒤에 `gender`, `name`이 출력? &rarr; 네이버 디벨로퍼에서 사용 API 점검 &rarr; 필수 항목에 `회원이름` `성별` 선택해제
  3. `gender`와 `name`은 없어졌지만, 아직 JSON 형태로 출력되고 너무 길어서 마음에 안듬
  > **해결 방법**<br><br>
  완벽한 해결법을 찾진 못했지만, 우선 필수 항목에 `회원이름`을 추가해서 너무 길었던 `고유 id값` 대신 이름을 출력하게 했다.<br>
  ex) NAVER__김경렬<br><br>
  ***새로운 문제 발견***<br>
  DB MEMBER 테이블에 username이 `NAVER__김경렬`과 같이 저장되어서 동명이인이 네이버 로그인으로 가입할 경우, username 컬럼은 UNIQUE 속성이기에 오류가 날 수 있다!<br><br>
  &rarr; 임시 고유 값을 추가해주거나 길어도 원래의 id를 출력하게 해야하나..? (해결 안됨)

<br><br>
**[특이사항]**

**1) 네이버 로그인의 경우 yml 설정에서 `provider: naver: user-name-attribute: response`를 해서 적용하면 JSON형태로 나와서 다른 소셜 로그인과 다른 형태로 출력이 된다. 이 문제를 어떻게 해결할 수 있을까?**
  1. 그냥 쓴다? &rarr; 보기 안좋음
  2. `{id=}`를 제거시켜 순수 id값만 뽑아낸다? &rarr; 1번보단 낫지만 고유 id값이 너무 길어서 보기 안좋음
  3. 이름이나 별명같이 그 사람의 정보를 필수 항목으로 받아서, `NAVER__(이름)` `NAVER__(별명)` 형태로 나오게 한다? &rarr; 동명이인 또는 같은 별명을 지닌 사람들은 최초 로그인 시 DB에 저장할 때, username이 UNIQUE 옵션이기 때문에 에러가 발생할 수 있음
  4. 새로운 고유 값을 생성해서 붙여준다? &rarr; 그나마 지금 당장 생각으로는 가장 문제 없는 방법

**2) 또한 네이버 API에서 성별을 필수 항목으로 선택하면 JSON 형식으로 `{id=ㅇㅇㅇㅇ, gender=M}` 이런식으로 나온다. 그렇다면 여기에서 나오는 `gender=M` 속성으로 최초 가입 시에 `gender=U`로 DB에 저장하지 않고, `gender=M`으로 저장시킬 수 있지 않을까?**

### 번외)
**3) 인스타 아이디는 언제든지 변경이 가능하다. 그렇다면 만약 A라는 사람이 `test1`이라는 인스타 아이디를 쓰고 있고, B라는 사람이 `test2`라는 아이디를 쓰고 있었다. 시간이 흐른 뒤, A라는 사람이 자신의 인스타 아이디를 `test3`으로 변경했고, B라는 사람은 `test1`이라고 변경했다. 이런 경우 아무런 문제가 발생하지 않을까?<br> 
&rarr; 확인해보니 역시나 문제가 발생한다. user4의 인스타 아이디는 원래 insta_user4고, user3의 인스타 아이디는 원래 insta_user3인데, user4가 test1로 인스타 아이디를 변경하여 그램그램에서도 내 인스타 아이디를 test1로 바꿔줬음에도, user3이 insta_user4 라는 인스타 아이디로 변경 등록하려하니 에러가 발생했다.(이미 사용중인 인스타 아이디입니다.)**
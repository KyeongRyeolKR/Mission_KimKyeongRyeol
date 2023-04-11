## Title: [2Week] 김경렬

### 미션 요구사항 분석 & 체크리스트

---

- [x] 호감표시 할 때, 예외처리 케이스 3가지 추가
  ##### [요구사항]
  1. `username`과 `attractiveTypeCode`가 완전히 같은 호감 추가 불가 
  2. 호감 표시는 10명까지만 가능
  3. `username`은 같지만, `attractiveTypeCode`가 다르면 기존 호감 표시 수정
  
  <br><br>

- [x] 네이버 로그인 연동
  ##### [요구사항]
  1. 로그인 페이지에서 네이버 로그인 버튼을 누르면 가입 및 로그인 처리가 되어야 한다.
  2. 최초 로그인 시, 가입이 되고 그 이후부터는 가입은 되지 않는다.
  3. 출력 양식 : `NAVER__[고유ID]`


### 2주차 미션 요약

---

**[접근 방법]**

## **1) 예외처리 1번, 3번(통합) 케이스 추가**
### [1차 작업]
- **[예외처리 1번]**
  1. 서비스 클래스에서 내가 한 호감표시 리스트를 가져온다.
  2. 모든 리스트를 뒤져서 `username`과 `attractiveTypeCode`가 같은 호감을 찾는다.
  3. 만족하는 조건의 호감 데이터가 존재한다면, 실패한 `RsData`를 반환한다.


- **[예외 처리 3번]**
  1. `attractiveTypeCode`를 변경하기 위해 `LikeablePerson` 엔티티에서 `attractiveTypeCode`에 `@Setter` 어노테이션 추가
  2. 서비스 클래스에서 내가 한 호감표시 리스트를 가져온다.
  3. 모든 리스트를 뒤져서 `username`는 같지만, `attractiveTypeCode`가 다른 호감을 찾는다.
  4. 만족하는 조건의 호감 데이터가 존재한다면, `setAttractiveTypeCode()` 메소드를 사용해 값을 변경하고, 변경한 내역 메세지를 담은 `RsData`를 반환한다.

> **문제**
> 1. 반복문에 조건문까지 복잡해서 코드의 가독성이 매우 떨어진다.
> 2. 조건문에 반복적인 코드가 발생한다.<br>
> &rarr;`if(isSameUsername(likeablePerson, username) && isSameTypeCode(likeablePerson, attractiveTypeCode))`<br>
> &rarr;`if(isSameUsername(likeablePerson, username) && !isSameTypeCode(likeablePerson, attractiveTypeCode))`

### [2차 작업]
1. `findByUsername()`, `isDuplicate()`, `canModify()` 메소드를 도입해 코드의 복잡성을 조금이나마 해결했다.<br>
&rarr; `findByUsername()` 메소드의 반환 타입으로 `Optional`을 사용함으로써 가독성을 높이고, 효율적인 `null` 또는 `empty` 처리 가능
> **문제**
> 1. 하지만 아직도 조건문의 반복적인 코드를 해결하지 못했다.<br>
> &rarr; `if (isDuplicate(oFound.get(), username, attractiveTypeCode))`<br>
> &rarr; `if (canModify(oFound.get(), username, attractiveTypeCode))`

### [3차 작업]
1. 필요없는 조건문이 작성되었다는 것을 인지했다. 이미 `findByUsername()`으로 `username`에 대한 비교를 했기에 더이상 `username`의 비교는 필요 없다.
2. 그에 따라 `isDuplicate()`, `canModify()` 메소드가 필요 없어진다. &rarr; 삭제
3. 조건문을 전체적으로 다음과 같은 로직으로 수정한다.<br>
&rarr; `username`이 같은 호감표시가 존재한다면, `attractiveTypeCode`를 비교한다.<br>
만약 `attractiveTypeCode`가 같을 경우, 실패한 `RsData`를 반환한다. (중복 호감 표시)<br>
만약 `attractiveTypeCode`가 같지 않은 경우, `setAttractiveTypeCode()`로 호감 사유를 수정하고 성공한 `RsData`를 반환한다.
> **문제**
> 1. 과연 엔티티의 값을 변경할 때, `setter`를 사용해도 되는 것인가? 에 대한 의문 생김<br>
> &rarr; 객체 지향 프로그래밍의 원칙 중 하나인 캡슐화를 지키기 위해 `setter`는 지양해야한다.
> * ***캡슐화 : 객체의 속성과 메서드를 하나로 묶고 외부에서 직접 접근하지 못하게 하는 것을 말한다. 캡슐화를 지키면 객체의 내부 상태가 보호되어 외부에서의 잘못된 접근으로 인한 오류를 방지할 수 있다.***


### [4차 작업]
1. 캡슐화의 원칙을 지키기 위해 `LikeablePerson` 엔티티 클래스에 `updateAttractiveTypeCode()` 메소드 추가
2. `updateAttractiveTypeCode()` 메소드는 변경하려는 `attractiveTypeCode`에 대해 유효성을 검사하고 올바른 값일 경우에만 값을 변경한다.
3. 서비스 클래스의 `setAttractiveTypeCode()`를 `updateAttractiveTypeCode()`로 변경했다.
> **문제**
> 1. 2차 작업에서 `findByUsername()` 메소드 분리는 했었다. 그러나 호감사유에 대한 로직은 조건문만 메소드화 시켰지 로직 자체를 분리하지 않아서 `like()` 메소드에 총 15줄(주석포함)이 추가되었다.<br>
> &rarr; `attractiveTypeCode`를 비교하는 로직을 메소드화 시켜야 함

### [5차 작업]
1. 메소드 분리를 위해 `checkDuplicateOrModifyByTypeCode()` 메소드 생성
2. 완전 중복이면 실패한 `RsData`를 반환하고, 호감 사유가 다르면 호감 사유 수정을 하고 성공한 `RsData`를 반환한다.
3. 호감 사유 수정을 했을 때, 수정 내역을 보이기 위해 `username`이 필요했다. 해당 값을 매개변수로 받을 수도 있었지만, 매개변수를 최소한으로 쓰고 싶어서 `username`을 매개변수로 받지 않고, 매개변수로 받았던 `likeablePerson` 객체에서 뽑아 따로 저장했다.
> **문제**
> 1. 과연 네이밍은 적절했는가? &rarr; 더 짧게, 더 명확하게?
> 2. 매개변수들은 적절했는가? &rarr; 매개변수를 늘리더라도 메소드 내 코드를 한줄이라도 줄였어야했나?

## **2) 예외처리 2번 케이스 추가**
### [1차 작업]
1. 단순히 `내가 한 호감표시 리스트의 크기가 10과 같거나 크다`라는 조건문을 생성한다.

### [2차 작업]
1. 큰 문제는 아니지만 조금 더 나은 가독성을 위해 `if(fromInstaMember.getFromLikeablePeople().size() >= 10)`의 조건을 `canAdd()` 메소드로 분리했다.<br>
&rarr; `if(!canAdd(fromInstaMember.getFromLikeablePeople()))`

>**문제**
> * 이 부분은 과연 메소드로 분리하는 것이 맞는가? 아니면 그냥 size를 명시적으로 보여주는 것이 맞는가?

## **4) 네이버 로그인 구현**
### [1차 작업]
1. 지난 주에 이미 구현해놓았던 기능이여서 단순히 `replace()`와 `substring()`을 사용해 `NAVER__(이름)` &rarr; `NAVER__(고유ID)`로 수정
> **문제**
> 1. `oauthId` 값이 다른 소셜 로그인들과 다르게 네이버는 JSON 형태로 주어져서 `replace()`와 `substring()`으로 잘라줬는데, 만약 다른 사용자 정보로 출력 형태를 바꾸게 되면 또 다시 알고리즘을 바꿔야하는 문제 인식

### [2차 작업]
1. [네이버 디벨로퍼 공식 문서](https://developers.naver.com/docs/login/profile/profile.md) `응답 예시` 파트에서, 받아오는 사용자 정보가 어떤 JSON 형태인지 확인함
2. 공식 문서를 보면, 먼저 `"resultCode":(값)`, `"message":(값)`, `"response":(값)`의 형태로 속성들이 주어지기 때문에 해당 모든 속성들을 `Map<String, Object>` 타입으로 받는다.
3. 내가 필요한 건 해당 속성들 중 `"response"`의 값이다. 하지만 `"response"` 속성의 값 또한 `Map<String, Object>`의 형태이기에 `Map<String, Object>` 타입으로 `response`를 받는다.
4. 최종적으로 받은 `"response"`에서 `"id"`의 값만 필요하기에 `oauthId`에 `"response"`의 `"id"`의 값을 저장한다.
## Title: [3Week] 김경렬

### 미션 요구사항 분석 & 체크리스트

---

- [x] 호감 표시 및 호감 사유 변경 시, 쿨타임 적용
  ##### [요구사항]
    1. 호감 표시 및 호감 사유 수정을 한 후, 각 호감 마다 3시간 쿨타임 적용
    2. 개별로 적용되어야 함
    3. 현재 UI로 쿨타임이 적용되어 있지만, 백엔드 로직으로 한번 더 체크해야함

<br><br>

- [x] 알림 기능 구현
  ##### [요구사항]
    1. 사용자가 호감을 받았거나, 기존 호감에 대한 사유 변경이 일어났을 경우 알림 페이지에서 확인이 가능해야함
    2. 각각의 알림은 생성 시, `readDate`가 `null`이고, 사용자가 읽으면 `readDate`가 `현재날짜`가 되어야한다.

### 3주차 미션 요약

---

**[접근 방법]**

## **1) 호감 쿨타임 적용**

   1. `LikeablePersonController`에서 수정이 가능한지 `isModifyUnlocked()` 메소드로 체크한다.
   2. 수정이 불가능하면(쿨타임이 남아있으면), `rq.historyBack()`을 호출한다.
   3. 수정 불가일 때, 메시지 출력 형식이 `n분 후에 변경/취소 할 수 있습니다.`로 나오게 하기 위해, `LikeablePerson` 엔티티에 `getModifyUnlockDateRemainStrHuman()` 메소드를 구현한다.


> **문제**
> 1. `isModifyUnlocked()` 메소드의 네이밍이 부정적인 단어여서, 메소드의 기능을 이해하는데 어려움이 있다.<br>
> &rarr; 해결 : **메소드 네이밍 변경 및 로직 수정** `isModifyLocked()`
> 2. 위 메소드의 네이밍이 변경되면서 `getModifyUnlockDateRemainStrHuman()` 메소드의 네이밍 변경에 대한 필요성을 느낀다. 또한, 좀 더 명확한 의미전달을 할 수 있는 네이밍을 고려하게 된다.<br>
> &rarr; 해결 : **메소드 네이밍 변경** `getFormattedRemainTimeForModify()`

## **2) 알림 기능 구현**

### [1차 작업] : 호감이 생성 됐을 때 알림 구현

1. 먼저 `notificationService`에 `add()` 메소드를 구현한다.<br>
&rarr; `add(LikeablePerson)`  : `builder`를 통해, 매개변수로 받은 `LikeablePerson` 객체 정보로 새 알림 객체를 생성하고 저장한다.
2. `list.html`의 UI로 생성된지 얼마나 된 알림인지, 어떤 사유 때문에 좋아하는지를 표현하기 위해 `Notification` 엔티티에 `getTimesAgo()`, `getOldAttractiveTypeDisplayName()`,`getNewAttractiveTypeDisplayName()` 메소드를 정의한다.<br>
3. 최종적으로 `LikeablePersonService`의 `like()` 메소드 안에 만들어둔 `add()` 메소드를 넣어준다.
> **문제**
> 1. `add()` 메소드 네이밍이 애매모호 한 것 같다.<br>
   &rarr; 해결 : **메소드 네이밍 변경** `like()`

### [2차 작업] : 호감 사유가 수정 됐을 때 알림 구현

1. 먼저 `notificationService`에 `modify()` 메소드를 구현한다.<br>
&rarr; `modify(LikeablePerson, int)`  : `builder`를 통해, 매개변수로 받은 `LikeablePerson` 객체 정보와 기존 호감 사유 코드로 새 알림 객체를 생성하고 저장한다.
2. 최종적으로 `LikeablePersonService`의 `modifyAttractive()` 메소드 안에 만들어둔 `modify()` 메소드를 넣어준다.
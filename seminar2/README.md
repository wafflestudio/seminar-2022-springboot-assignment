
# 세미나 2 과제
### Due: 2022.10.30.(월) 23:59
- REST API의 주된 인증 방식인 JWT 방식을 이해하고, Spring 프레임워크 위에 인증을 적절히 처리하는 로직을 구성합니다.
- 온전히 동작하는 하나의 애플리케이션을 주도적으로 개발하는 경험을 가지게 됩니다.
- one-to-many(many-to-one), one-to-one 등 RDB의 연관관계를 고려해 좀 더 주체적인 구현을 할 수 있게 됩니다.
- 여러 API에서 각종 권한 및 예외 처리를 익히고, 좀 더 복합적인 서비스 로직을 다루는 연습을 합니다.


### 안내사항
- 앞으로는 H2를 통해 개발하지 않고, MySQL 로컬 DB와 연동하는 것이 필수 요구사항입니다. 적절히 연결해주세요!
- Third party library는 기본적으로 허용됩니다. 출제 의도를 벗어날 정도의 의견을 제공하는 라이브러리는 금지되니, 논란이 될만한 라이브러리 사용은 문의주세요.
- 더 많이, 더 디테일하게 구현할수록 점수가 가산됩니다. 아래 명세는 필수 요구사항이지만, 앱을 구성하기 위해 필요하다고 판단되는 것이 있다면 구현사항을 추가해서 구현해주세요.
  - 단, 아래 명세에 등장하는 Json Spec 들의 경우 이름이나 타입은 자율적으로 가져가도 괜찮아요.
  - 다만 있어야 하는 정보가 누락되서는 안돼요.

- 다음 세미나 마감 전까지, PR을 올려주세요.
  - 어디까지 (얼마나 자세히) 확인하고 공유할지는, 조 별로 **자율적으로** 정해주세요. 당연하지만 적극적으로 좋은 퀄리티를 위해 고민해주세요.
  - 이를 바탕으로 Peer Review를 진행할 수 있도록 합니다.
  - ❗️ 조원들에 대한 Peer Review는, 과제 마감 후 **이틀 내**에 완료해주세요! ❗️

---
## 회원가입, 로그인, 인증 구현하기

- JWT 인증을 구현합니다.
- 과제 1에서 그랬듯이, 유저는 고유한 이메일을 가질거에요.
- 회원가입에서, 유저는 이메일 & 이름 & 비밀번호를 입력합니다.
  - [SignUpRequest](./src/main/kotlin/com/wafflestudio/seminar/core/user/api/request/SignUpRequest.kt) DTO를 확인해주세요.
  - 회원가입에 성공하면, 유저는 AccessToken (JWT)를 응답으로 받아야 합니다.
- 이후 로그인을 하는 경우에도, 유저는 이메일 & 비밀번호를 입력하고, accessToken을 제공받습니다.
- 이후 인증이 필요한 API에서, 유저는 `Authorization: Bearer {token}`의 형태로 헤더를 제공합니다.
  - 적절히 파싱해서, 유저 정보를 컨트롤러에 넘길 수 있도록 구성해주세요.
  - [AuthTokenService](./src/main/kotlin/com/wafflestudio/seminar/core/user/service/AuthTokenService.kt)에 JWT 관련 스켈레톤이 있습니다.
  - [AuthProperties](./src/main/kotlin/com/wafflestudio/seminar/core/user/service/AuthProperties.kt)는 application.yaml 파일의 설정값을 읽어오도록 설계되었습니다.
    - 코드 상의 정보를 바탕으로, 설정 파일을 채워주세요.
    - 발급 주체는 여러분이니까, 임의의 문자열이면 되고, 만료 정보는 초 단위의 Long 데이터로, 여러분 생각에 적절한 수준으로 구성해주세요.
  - 이후 과제의 모든 API는 인증을 필요로 한다고 가정하겠습니다.
  - 뒤에 나오는 수강생-진행자 여부의 경우 여기서 인증하지 않아도 돼요!!

## 세미나 수강 신청 서비스 만들기

- 세미나 도메인을 추가해볼게요 :)
  - 이후 과제에서 다양한 개념들(= 테이블로 저장될만한 모델들)이 등장해요.
  - 관련 코드들은, 스켈레톤의 core 패키지 아래에, 자율적으로 설계해주세요.
- 사용자(유저)는 세미나와 다대다 관계를 가집니다.
  - UserSeminar 테이블을 매핑 테이블로 관리하고 싶어요.
  - UserSeminar 테이블에 넣는 정보는 자율적으로 정해주세요.
    - 단, 생성 시각 & 수정 시각은 꼭 포함되어야 해요. (BaseTimeEntity 참고 !!)
  - 연결된 Seminar 또는 User가 삭제되는 경우에는, UserSeminar 테이블도 삭제되게 해주세요. (@ManyToOne-cascade 옵션을 참고해주세요.)

### 수강생과 진행자

- 사용자는, 세미나와 연결지어 생각해보면, **수강생과 진행자**가 있을 것 같아요. 
- 사용자는 진행자이면서 수강생일 수도 있습니다. 둘 다 아닐 수는 없습니다.
- 수강생인 유저는 `ParticipantProfile` 테이블에, 진행자는 `InstructorProfile` 테이블에 프로필을 기록해둘거에요.
- 유저 테이블은 각각의 테이블과 @OneToOne 관계를 가집니다. ORM 상에 이를 적절히 정의해주세요.
- 이 테이블들도 BaseTimeEntity를 상속하게끔 해주세요.
- 그 외의 column들은 아래의 과제 내용을 참고하여 자유롭게 구현해도 좋습니다. 직접 정보를 관리하고 로직을 만들기 좋은 구조를 생각해 이용하되, API의 request와 response에 대한
  스펙은 정확히 지켜야 합니다. 

### 회원가입 때 프로필도 같이 받기

- 회원가입 Request Body 안에 `role` 필드를 정의하고, 이 값에 따라 앞서 정의한 프로필 테이블이 함께 생성되도록 할게요.
- `role` 필드는 enum 또는 스트링으로 표현해주세요. 수강생, 진행자 포맷을 정해주시고, 잘못 들어오는 값들은 `400` 응답을 적절한 메시지와 함께 내려주시면 될 것 같아요.


- **아래 정보를 바탕으로, 테이블을 적당히 수정하고, 회원가입 시에도 정보를 받을 수 있도록 해주세요.**
    - 수강생인 사용자는 소속 대학에 대한 정보(university)를 저장할 수 있습니다. 정보가 없는 경우, `""`으로 DB에 저장됩니다.
      - 수강생인 사용자는 활성회원인지 여부(isRegistered)를 받을 수 있습니다. 정보가 없는 경우, `true`가 디폴트 값이에요.
    - 진행자인 사용자는 소속 회사에 대한 정보(company)와 자신이 몇 년차 경력인지(year)를 저장할 수 있습니다. 정보가 없는 경우, 각각 `""`과 null으로 DB에 저장됩니다.
      - year에 0 또는 양의 정수가 아닌 값이 오는 경우는 `400`으로 응답하며, 적절한 에러 메시지를 포함합니다.
  

- 참여자인 User가 request body에 company를 포함하는 등, 자신의 유형과 맞지 않는 정보가 들어오면 그냥 무시하면 됩니다. body가 완전히 비어있어도 무시하면 됩니다.
  

### 프로필을 읽어오고, 수정할 수 있도록 하기

- `GET /api/v1/user/{user_id}/`를 정의하고, API의 response body를 아래 json spec처럼 구성해서 내려주세요.
- null이 가능하다고 명시하지 않은 값엔 null이 들어가면 안 됩니다. 
- 처음부터 이와 같은 body를 갖출 수는 없고, 아래에 기술되는 과제 내용을 참고하여 개발이 진행되어감에 따라 이에 가까워질 것입니다.
````json
{
    "id": User id,
    "username": User username,
    "email": User email,
    "lastLogin": User last_login,
    "dateJoined": User date_joined,
    "participant": {
        "id": ParticipantProfile id,
        "university": ParticipantProfile university(string),
        "isRegistered": ParticipantProfile isRegistered(bool),
        "seminars": [
            {
                "id": Seminar id,
                "name": Seminar name(string),
                "joinedAt": UserSeminar joinedAt(datetime),
                "isActive": UserSeminar isActive(bool),
                "droppedAt": UserSeminar droppedAt(datetime, 정보가 없는 경우 null)
            },
            ...
        ] (참여하는 Seminar가 없는 경우 [], is_active가 false인 Seminar도 포함)
    } (ParticipantProfile이 없는 경우 null),
    "instructor": {
        "id": InstructorProfile id,
        "company": InstructorProfile company(string),
        "year": InstructorProfile year(number, 정보가 없는 경우 null),
        "instructingSeminars": {
            "id": Seminar id,
            "name": Seminar name(string),
            "joinedAt": UserSeminar joinedAt(datetime)
        } (담당하는 Seminar가 없는 경우 null)
    } (InstructorProfile이 없는 경우 null)
}
````
---
- `PUT /api/v1/user/me/`를 구현해주세요.
- 참여자인 User는 소속 대학에 대한 정보(university)를 수정할 수 있습니다. 정보가 없는 경우, `""`으로 DB에 저장됩니다.
- 진행자인 User는 소속 회사에 대한 정보(company)와 자신이 몇 년차 경력인지(year)를 수정할 수 있습니다. 정보가 없는 경우, 각각 `""`과 null으로 DB에 저장됩니다.
- 위의 정보와 함께, 이메일을 제외한 본인의 모든 정보 또는 일부를 마찬가지로 수정할 수 있어야 합니다.
  - 활성회원 여부 (isRegistered)는 수정할 수 없겠죠!?
- 참여자인 User가 request body에 company를 포함하는 등, 자신의 유형과 맞지 않는 정보가 들어오면 그냥 무시하면 됩니다. body가 완전히 비어있어도 무시하면 됩니다.
- university와 company에 0글자가 오는 경우에는 ""으로 수정하면 됩니다.
- year에 0 또는 양의 정수가 아닌 값이 오는 경우는 `400`으로 응답하며, 적절한 에러 메시지를 포함합니다.

### 진행자도 세미나 수강이 하고 싶어요!
- 기존에 진행자로 가입한 경우, 나중에 참여자로 등록할 수도 있습니다. 
- `POST /api/v1/user/participant/`를 통해 이를 가능하게 해주세요.
- request body도 적절히 구성해주세요. 대학교와, 활성회원 여부는 각각 `""`, `True`가 기본값입니다.
- 이미 참여자인 사람이 또 요청하면 `409`로 응답하며, 적절한 에러 메시지를 포함해주세요. 성공적인 경우 `200`로 응답하고 위의 json spec과 같은 구조로 응답을 내려주세요.
- 한 번 진행자 또는 참여자가 된 User는 서비스 로직상 해당 Profile을 잃지 않습니다.

### 세미나를 만들 수도 있어요
- `POST /api/v1/seminar/`를 seminar app 내에 구현해주세요.
- request body는 세미나 이름(name), 정원(capacity), 세미나 횟수(count), 정기 세미나 시간(time), 온라인 여부(online)를 포함해야 합니다.
  online 여부 외에는 하나라도 빠지면 `400`으로 응답하며, 적절한 에러 메시지를 포함합니다.
- name에 0글자가 오는 경우, capacity와 count에 양의 정수가 아닌 값이 오는 경우는 `400`으로 응답합니다. 
- time은 "14:30"과 같은 형식으로 받을거에요. 타입 및 저장에 관해서는 자율적으로 진행해주세요.
- online 여부는 `True`를 기본으로 합니다. 
- 성공 시 응답은 대략적으로 아래와 같아야 해요. 
- 이 API는 세미나 진행자 자격을 가진 User만 요청할 수 있으며, 그렇지 않은 경우 `403`으로 응답합니다. 요청한 User가 기본적으로 해당 Seminar의 담당자가 됩니다.
````json
{
    "id": Seminar id,
    "name": Seminar name,
    "capacity": Seminar capacity,
    "count": Seminar count,
    "time": Seminar time,
    "online": Seminar online,
    "instructors": [
        {
            "id": User id,
            "username": User username,
            "email": User email,
            "joinedAt": UserSeminar joinedAt(datetime)
        },
        ...
    ]
    "participants": [
         {
            "id": User id,
            "username": User username,
            "email": User email,
            "joinedAt": UserSeminar joinedAt(datetime)
            "isActive": UserSeminar isActive(bool),
            "droppedAt": UserSeminar droppedAt(datetime, 정보가 없는 경우 null)
         },
         ...
    ] (참여하는 Participant가 없는 경우 [], isActive == false인 Participant도 포함)
}
````

- 똑같은 내용을 담아, `PUT /api/v1/seminar` API도 구현해주세요.
  - 만든 사람이 아니면 수정을 할 수 없어요. 그 경우에는 403 처리를 해주세요.

### 세미나를 불러올 수도 있어요

- `GET /api/v1/seminar/{seminar_id}/`로 Seminar의 정보를 가져올 수 있습니다. `seminar_id`에 해당하는 Seminar가 없는 경우 `404`로 응답합니다.
  세미나 생성 API와 동일한 구조의 body와 함께 `200`으로 응답합니다.

- `GET /api/v1/seminar/`로 여러 Seminar의 정보를 가져올 수 있으며, status code는 `200`입니다.
- 이 때, 선택적으로 request에 query params를 포함할 수 있습니다.
    - `GET /api/v1/seminar/?name={name}`으로 query param이 주어지면, {name} str을 포함하는 name을 가진 Seminar들을 모두 가져옵니다.
      해당하는 Seminar가 없으면 `[]`를 body로 합니다.
    - response는 아래와 같으며, 기본적으로 Seminar의 created_at을 기준으로 가장 최근에 만들어진 Seminar가 위에 오도록 body를 구성합니다.
      ````json
      [
          {
              "id": Seminar id,
              "name": Seminar name,
              "instructors": [
                  {
                      "id": User id,
                      "username": User username,
                      "email": User email,
                      "joinedAt": UserSeminar joinedAt(datetime)
                  },
                  ...
              ],
              "participantCount": Seminar에 Participant로 참여 중인 User의 수 (isActive == false인 Participant는 참여 중인 것이 아니므로 제외)
          }
      ]
      ````
    - `GET /api/v1/seminar/?order=earliest`으로 query param이 주어지면, Seminar의 created_at을 기준으로 가장 오래된 Seminar가 위에
      오도록 body를 구성합니다. order에 `earliest`가 아닌 값들이 오는 경우는 무시하고 기본적인 최신 순으로 정렬하면 됩니다.
      그 외 name, order가 아닌 query param key가 포함되는 경우도 무시하면 됩니다. name, order는 함께 적용할 수 있으며, 두 param 모두 없으면
      전체 Seminar를 최신 순으로 정렬하면 됩니다.

### 세미나에 참여하거나, 세미나 함께 진행하기
- 진행자는 다른 세미나 진행에 참여할 수도 있고, 수강생은 다른 세미나를 수강할 수도 있어요.
- 두 동작은 모두 `POST /api/v1/seminar/{seminar_id}/user/`를 통해 이뤄집니다. 
  - `seminar_id`에 해당하는 Seminar가 없는 경우 `404`로 응답합니다. 
  - request body에 `role`의 값이 `participant` 또는 `instructor`인지에 따라 어떤 자격으로 해당 세미나에 참여하는지 결정되며,
    `seminar_id`을 통해 어떤 Seminar에 참여할지 결정됩니다.
  - `role`이 둘 중 하나의 값이 아닌 경우 `400`으로 적절한 에러 메시지와 함께 응답하며,
    해당하는 자격을 가지지 못한(ParticipantProfile이 없는 User가 `participant`로 요청하는 등) User가 요청하는 경우 `403`으로 적절한 에러 메시지와 함께 응답합니다.
- Participant 자격을 갖고 있다고 하더라도, 활성회원이 아닌 사용자가 세미나에 참여하는 요청을 하는 경우 `403`으로 적절한 에러 메시지와 함께 응답합니다.
- `participant`로 Seminar에 참여할 때, Seminar의 capacity가 해당 Seminar에 참여자로서 있는 User들로 인해 이미 가득 찬 경우 `400`으로 응답하며 적절한 에러 메시지를 포함합니다.
- Instructor 자격을 갖고 있다고 하더라도, 자신이 이미 담당하고 있는 세미나가 있는 경우 `400`으로 적절한 에러 메시지와 함께 응답합니다.
- 이미 어떤 자격으로든 해당 세미나에 참여하고 있다면 `400`으로 적절한 에러 메시지와 함께 응답합니다.
- 성공적으로 처리된 경우, 해당 세미나에 대해 세미나 생성 API와 동일한 구조의 body와 함께 `200`로 응답합니다.
- 위 json spec 내부 joinedAt은 세미나 진행자 또는 세미나 참여자가 해당 세미나를 시작한 시점을 나타냅니다.

### 세미나 드랍하기
- `DELETE /api/v1/seminar/{seminar_id}/user/`를 통해 세미나 참여자는 해당 세미나를 중도 포기할 수 있습니다.
  `seminar_id`에 해당하는 Seminar가 없는 경우 `404`로 응답합니다. 세미나 진행자가 해당 요청을 하면 `403`으로 적절한 에러 메시지와 함께 응답합니다.
- 정상적으로 처리되는 경우, 해당 User와 Seminar의 관계에서 is_active를 False로 설정하고, 그 시점을 dropped_at으로 저장하며, 세미나 생성 API와
  동일한 구조의 body와 함께 `200`으로 응답합니다.
- 중도 포기한 세미나는 `POST /api/v1/seminar/{seminar_id}/user/`를 이용해 다시 참여할 수 없습니다. 이 경우 `400`으로 적절한 에러 메시지와 함께 응답합니다.
- 해당 세미나에 참여하고 있지 않은 경우, 무시하고 그냥 `200`으로 응답하면 됩니다.

## 요청 별로 시간 로깅하기
- 세미나에서 AOP를 통한 로깅을 어떻게 하는지 배웠어요.
- 위 애플리케이션을 구현하면서, 각 요청의 수행 시간을 기록해볼게요.
- @Aspect와 Logger를 활용해서 적절히 요청 별 수행시간을 로그로 남겨주세요!
- 그리고 요청이 얼마나 걸리는지 한번 체크해주세요.
- **모든 요청은 2초 이내에 수행되어야 합니다!!**

## (추가 구현거리) 페이지네이션

- 앞선 API들은 현재 페이징 처리가 되어있지 않습니다.
- 데이터가 많아질 나중을 대비해, 모든 데이터가 50개씩 끊어서 내려갈 수 있도록 페이징 처리를 해주세요.
- (해당 과제는 필수 요구사항은 아니지만, 구현 시 추가 점수가 부여됩니다. 구현하신 경우 PR에 이를 언급해주세요.)

--- 
### 당부의 말씀

> 공식문서, 구글링을 두려워하지 마세요!

- 질문을 많이 해주세요 !!!
    - 이번 과제는 0부터 시작하는 터라, 막막한 부분도 많을 거에요. 애매하다, 궁금하다 싶으면 질문을 많이 해주세요 :)

---
### Hint

- Json Spec에서,,
```json
{
    "id": User id,
    "username": User username,
    "email": User email,
    "joinedAt": UserSeminar joinedAt(datetime)
}
```
- 이런 데이터들을 보면, UserSeminar 테이블과 User 테이블에서 동시에 정보를 가져와야 해요.
- QueryDSL Projection을 적극적으로 사용하면 좋아요.
- 또 아래와 같은 것들을 고민해보세요.
  - 필요에 따라 어떤 관점에서 SQL 질의를 작성할 것인가,
  - leftJoin, innerJoin : 어떤 기준으로 나뉘는가 등등..

- 갑자기 난이도가 수직상승하긴 했지만, 배운 것들을 바탕으로 차근차근 고민해보면 쉽게 풀 수 있을 거에요.
- 수업에서 제가 Entity-DTO 매핑을 보여드릴 시간이 없을 수도 있을 것 같은데,
  - 만약 이걸 읽는 시점에서 실제로 그렇다면,
  - 슬랙에 저를 태그해주세요 ㅎㅎ

---

### 참고하면 좋은 레퍼런스

- [Validation 관련 문서](https://www.baeldung.com/spring-service-layer-validation)
- https://www.baeldung.com/

- https://docs.spring.io/spring-boot/docs/2.5.4/reference/pdf/spring-boot-reference.pdf

- 앞으로도 늘 그렇겠지만, 과제를 진행하며 모르는 것들과 여러 난관에 부딪히리라 생각됩니다. 당연히 그 지점을 기대하고 과제를 드리는 것이고, 기본적으로 스스로 구글링을
  통해 여러 내용을 확인하고 적절한 수준까지 익숙해지실 수 있도록 하면 좋겠습니다.

- 문제를 해결하기 위해 질문하는 경우라면, 질문을 통해 기대하는 바, (가급적 스크린샷 등을 포함한) 실제 문제 상황, 이를 해결하기 위해 시도해본 것, 예상해본 원인 등을 포함시켜 주시는 것이 자신과 질문을 답변하는 사람, 제3자 모두에게 좋습니다.

- 저는 직장을 다니고 있으므로 아주 빠른 답변은 어려울 수 있고, 특히 과제 마감 직전에 여러 질문이 올라오거나 하면 마감 전에 모든 답변을 드릴 수 있다는 것은
  보장하기 어렵다는 점 이해해주시면 좋겠습니다. 그리고 세미나 진행자들이 아니어도, 참여자 분들 모두가 자신이 아는 선에서 서로 답변을 하고 도우시려고 하면 아주 좋을 것 같습니다.

- 과제의 핵심적인 스펙은 바뀌지 않을 것이며 설령 있다면 공지를 따로 드릴 것입니다. 하지만 문구나 오타 수정 등의 변경은 수시로 있을 수 있고,
  특히 '참고하면 좋을 것들'에는 추가 자료들을 첨부할 수도 있습니다. 때문에 종종 이 repository를 pull 받아주시거나, 이 페이지를 GitHub에서 종종 다시 확인해주시기 바랍니다.

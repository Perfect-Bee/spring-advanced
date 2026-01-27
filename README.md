# SPRING ADVANCED

### ⚠️ 꼭 지켜주세요

**Given-When-Then 패턴**
https://cobi-98.tistory.com/53

**Git Commit**
- 단계별로 해당 과제의 대한 내용과 함께 커밋을 남겨주세요.
- ex1) commit message
```bash
레벨 1-1: Early Return
```

---
## Lv.0 : 프로젝트 세팅 : 실행 시 에러 발생
<details>
<summary> 열기/닫기 </summary>

 ### 에러 코드는 아래부터 확인하기
 5. IllegalArgumentException: Could not resolve placeholder 'jwt.secret.key'

![](https://velog.velcdn.com/images/parslime/post/57b44ae3-3af2-443f-88c0-648668035acc/image.png)

- ${jwt.secret.key} 라는 설정 값을 어디에서도 찾을 수 없다
- 이게 문제
### 해결방안
- jwt.secret.key가 없다고 한다. 찾아보자
- ../main/java/org/example/expert/config/JwtUtil.java에 있다.
![](https://velog.velcdn.com/images/parslime/post/ef321ae2-b83a-4640-a25b-f286d7302ef3/image.png)

- 여기에서 @Value("${jwt.secret.key}")는 application.properties에서 설정한 값을 가져오는 어노테이션이다.
- 일반적인 프로그램의 경우 프로젝트 생성 시 처음부터 깔려있다.

- 예) 지난 시간 팀 프로젝트 커머스에서도
![](https://velog.velcdn.com/images/parslime/post/f9057094-2af2-46e2-a7ad-20db2e5515f5/image.png)

- 처음부터 만들어져 있었다.

- 따라서, 해당 파일을 만들어보자. : ../main/resources/application.properties

![](https://velog.velcdn.com/images/parslime/post/29b60387-6386-4941-a3fd-6acfed6d8795/image.png)


#### 여기서 주의할 점 : 256bit 이상 입력해야 한다.
![](https://velog.velcdn.com/images/parslime/post/4c830f59-105a-4d14-8eaf-bbd8fa72fa35/image.png)

- !!! 실행 시 에러 발생 !!!
Caused by: io.jsonwebtoken.security.WeakKeyException: The specified key byte array is 0 bits which is not secure enough for any JWT HMAC-SHA algorithm.  The JWT JWA Specification (RFC 7518, Section 3.2) states that keys used with HMAC-SHA algorithms MUST have a size >= 256 bits (the key size must be greater than or equal to the hash output size).  Consider using the io.jsonwebtoken.security.Keys#secretKeyFor(SignatureAlgorithm) method to create a key guaranteed to be secure enough for your preferred HMAC-SHA algorithm.  See https://tools.ietf.org/html/rfc7518#section-3.2 for more information.

#### 이유 : JWT와 Filter 편 참고
Base64 vs Base64URL
Base64는 인코딩시 +, /, =를 포함하게 됩니다. 
특히 /와 =의 경우 URL or 파일 경로에 쓰이기 때문에 원치 않는 에러가 발생할 수 있습니다. 
따라서 +는 -, /는 _, =는 제거하여 에러 상황을 방지합니다.


그럼 왜 =는 다른 문자로 바꾸는 것이 아니라 제거하나요? 
Base64 특성상 인코딩 결과가 무조건 4의 배수로 나오게 됩니다. 
결과가 4의 배수가 안될 경우 =를 넣어 4의 배수로 맞춰주는데 이러한 포매팅 방식을 패딩이라고 합니다. 
이 특성으로 인해 Base64URL에서 =를 제거할 수 있는 것입니다. 
4의 배수에 맞춰 다시 =를 넣어주어 원래의 문자열 길이로 복구할 수 있으니까요!

- 256자 이상으로 다시 작성해보자.
(Base64로 인코더 해보자)
![](https://velog.velcdn.com/images/parslime/post/f2407cec-9e97-4313-926a-1d708817da9f/image.png)

![](https://velog.velcdn.com/images/parslime/post/f643b4f1-39b7-4d5a-83f9-bf8a3f9b8517/image.png)

문제가 해결됐으나, 새로운 문제가 있다.

그 점은 평소 사용하던 코드를 그대로 붙여넣기 해보자. (DB는 testAD로 새로 만들었다.)

![](https://velog.velcdn.com/images/parslime/post/2ee9a35d-1935-49d2-be01-e0e5bc168b0a/image.png)

![](https://velog.velcdn.com/images/parslime/post/ff081b8a-1d31-4a5e-96ae-0e8f2be44552/image.png)

실행이 잘 된다.

## 추가
application.properties를 가독성을 위해 application.yml로 바꿔보자
- 계층구조

![](https://velog.velcdn.com/images/parslime/post/8f0dcab1-d043-48b7-9403-136902817f1d/image.png)

</details>

---


## Lv.1 : ArgumentResolver에서 실수로 코드가 삭제된 상황
> 패키지 org.example.expert.config;에 위치한 AuthUserArgumentResolver 의 로직이 현재 동작하지 않고 있습니다. AuthUserArgumentResolver 가 정상적으로 기능할 수 있도록 해주세요.

<details>
<summary> 열기/닫기 </summary>
 
![](https://velog.velcdn.com/images/parslime/post/551a031d-0865-4b68-a161-a55fcd9402dd/image.png)

- 뭔가 빼먹어서 사라졌다.

- @Component 빼먹었다.
![](https://velog.velcdn.com/images/parslime/post/1e3cbbf7-9c4f-4f66-91ba-736e55afd32c/image.png)

- 근데, 이것만으로는 부족하다. MVC 등록 지점이 필요하다.
= bean + 등록지점
=> WebConfig.java가 없다. 만들자

![](https://velog.velcdn.com/images/parslime/post/263c4a95-6142-437f-b0db-48ac4398ce07/image.png)

</details>

---

## Lv.2 : 코드 개선
### Lv.2-1 코드 개선 퀴즈 - Early Return
> 조건에 맞지 않는 경우 즉시 리턴하여, 불필요한 로직의 실행을 방지하고 성능을 향상시킵니다.
해당 에러가 발생하는 상황일 때, `passwordEncoder`의 `encode()` 동작이 불필요하게 일어나지 않게 코드를 개선해주세요.

<details>
<summary>열기/닫기</summary>
 
![](https://velog.velcdn.com/images/parslime/post/1e43a245-6064-41d8-94bb-760b64e01b82/image.png)

- 이 코드가 하고자 하는 것은 회원가입 시 비밀번호를 암호화하여 저장하고, 이메일을 중복체크 하는 것이다.

- 문제는 이메일 중복 확인 전 비밀번호를 냅다 암호화 해버리는 것이니까
```
	[ 로그인 ]
	    ↓
[ 이메일 중복 확인 ]
		↓
 [ 비밀번호 암호화 ]
 		↓
   [ 권한 변환 ]
```

이 순서대로 진행하게끔 만들면 된다.
</details>



### Lv.2-2 리펙토링 퀴즈 - 불필요한 if-else 구조 해결
> 복잡한 if-else 구조는 코드의 가독성을 떨어뜨리고 유지보수를 어렵게 만듭니다. 불필요한 else 블록을 없애 코드를 간결하게 합니다.
<details>
<summary> 열기/닫기 </summary>
![](https://velog.velcdn.com/images/parslime/post/2dd56e24-3e0b-480a-b24f-047e57b0a396/image.png)

- 여기에서 불필요한 if-else 구조를 찾아보자.
- 해당 코드가 하고자 하는 것은 오늘 날씨에 대한 데이터를 출력하는 것이다.
- 그 순서는
1. 날씨 API를 호출한다
2. HTTP 상태 코드가(응답) 정상(200 OK)인지 확인한다
3. 날씨 데이터가 존재하는지 확인한다
4. 오늘 날짜에 해당하는 날씨를 찾는다
5. 찾으면 반환한다
6. 못 찾으면 예외를 던진다

문제는 if-else 구조를 썼으면서 if문에서 throw를 썼다. 해당하지 않으면 냅다 밖으로 던져버리기 때문에 else 코드가 실행되지 않는다. 이래선 if-else 쓴 의미가 없다. 따라서 분리해두자.
![](https://velog.velcdn.com/images/parslime/post/816beff9-6a31-414a-961e-0c082f861466/image.png)

</details>


### Lv.2-3 코드 개선 퀴즈 - Validation
> 패키지 org.example.expert.domain.user.service; 의 UserService 클래스에 있는 changePassword() 중 아래 코드 부분을 해당 API의 요청 DTO에서 처리할 수 있게 개선해주세요.

<details>
<summary> 열기/닫기 </summary>
 
![](https://velog.velcdn.com/images/parslime/post/e6781bc5-dd4c-4883-a718-b443ef377c7f/image.png)

> Tip : 'org.springframework.boot:spring-boot-starter-validation' 라이브러리를 활용해주세요!

![](https://velog.velcdn.com/images/parslime/post/f8e3bc4b-ee21-47de-a9b9-9401f63797f4/image.png)


서비스에서 너무 많은 요청을 처리하는 것 같다. 이걸 DTO에서 처리하면 userChangePasswordRequest.java를 찾아보자.

![](https://velog.velcdn.com/images/parslime/post/ffae8a17-bd99-47b2-9dcf-81f9ef9f5a07/image.png)

여기에 조건을 입력해두자.

![](https://velog.velcdn.com/images/parslime/post/64b2fac7-33ef-48e9-997b-8a6d0ba051e1/image.png)

수정했으니 서비스도 수정해주고

![](https://velog.velcdn.com/images/parslime/post/987c6022-fabe-4532-b131-a49ff5b93fd1/image.png)

DTO에서 유효한지 체크해야 하기에 컨트롤러에도 @Vaild 넣어두자
![](https://velog.velcdn.com/images/parslime/post/d8db2442-771e-4199-8d95-22d810f6c63a/image.png)

</details>

---

## Lv.3 : N+1 문제
> `TodoController`와 `TodoService`를 통해 `Todo` 관련 데이터를 처리합니다.
여기서 N+1 문제가 발생할 수 있는 시나리오는 `getTodos` 메서드에서 모든 Todo를 조회할 때, 각 Todo와 연관된 데이터를 개별적으로 가져오는 경우입니다.
- 요구사항:
JPQL `fetch join`을 사용하여 N+1 문제를 해결하고 있는 `TodoRepository`가 있습니다. 이를 동일한 동작을 하는 `@EntityGraph` 기반의 구현으로 수정해주세요.

<details>
<summary> 열기/닫기 </summary>

![](https://velog.velcdn.com/images/parslime/post/dfde0e48-c9e0-4394-bf83-805d6baa82de/image.png)

- 요구사항을 수행하기 위해 @Query를 @EntityGraph 기반으로 수정해보자.


해당 코드는 
1. TODO를 t라고 하고, t 선택
2. t목록을 내림차순으로 정리한다. t.user를 fetch join한다.(TODO와 t.user 같이 가져와)
-> 목록으로 가져와(전체조회)

1. TODO 하나를 조회
2. 그 조회된 TODO의 유저도 가져오고(TODO와 t.user 같이 가져와)
3. id가 todoId인 것을 가져와
-> TODO의 ID로 가져와(단건조회)

@EntityGraph는 JPQL 없이 연관된 엔티티를 패치 조인(Fetch Join) 방식으로 한 번에 조회하여 N+1 문제를 해결하는 어노테이션이다.

즉 쿼리 부분을 싹 날리고 @EntityGraph만 쓰면 된다.

![](https://velog.velcdn.com/images/parslime/post/cadaca63-b5ae-4e58-84c7-1386823cb337/image.png)


#### 문제 발생!
Caused by: java.lang.IllegalArgumentException: Failed to create query for method public abstract java.util.Optional org.example.expert.domain.todo.repository.TodoRepository.findByIdWithUser(java.lang.Long); No property 'withUser' found for type 'Long'; Traversed path: Todo.id

#### Long 형식에 WithUser 속성?
#### -> 자바스프링의 신비로운 기능인 레지스토리에 매서드명 자체에 기능이 딸려오는 효과로 인해 WithUser을 사용 시 쿼리문으로 인식되는 마법이 일어난다고 한다.
+ todoId 조회에 사용했던(:todoId에 어떤 메서드 파라미터를 넣을지 결정하는) @Param도 삭제하자.

#### 따라서 @EntityGraph로 수정했으니 @Param도 빼고, WithUser도 빼자.
![](https://velog.velcdn.com/images/parslime/post/37ec1150-5e29-44f7-8ce5-ea3b745ebfe7/image.png)

+ 주의! 서비스에서 사용한 이름도 바꾸자

![](https://velog.velcdn.com/images/parslime/post/acdcc206-d1f8-46ae-ac98-cc8b8d45be78/image.png)

↓ ↓ ↓

![](https://velog.velcdn.com/images/parslime/post/54f81707-4ecf-46c7-90b9-052262a490b3/image.png)

</details>

---

## LV.4 : 테스트 코드 연습

### LV.4-1 예상대로 성공하는가?
### LV.4-1-1 인코더 제대로 동작하는가?
<details>
<summary> 열기/닫기 </summary
					
테스트 코드 실행(../test/java/org.example/expert/config/PasswordEncoderTest.java)

![](https://velog.velcdn.com/images/parslime/post/6b4b152b-2ce3-4af1-b9b3-057375d2deb4/image.png)


에러 발생
![](https://velog.velcdn.com/images/parslime/post/e2d4c526-e482-4955-a668-f42b47ac0f24/image.png)

뭐가 문제라는 걸까?

일단 인코더 매서드부터 확인하자.
![](https://velog.velcdn.com/images/parslime/post/42588713-7e73-4832-a916-729b0b2ff82b/image.png)

#### 평소 내가 자주 하는 실수가 여기 있다.
- rawPassword와 EncodedPassword 순서가 다르다.
- Encoded raw가 아니라 raw Encoded이다.

![](https://velog.velcdn.com/images/parslime/post/375c146b-d3ab-40eb-b1f5-d0691250982e/image.png)

#### 어라... 왜 또 문제가 생기지?

![](https://velog.velcdn.com/images/parslime/post/c4af36fc-9a2e-4e81-8db3-522d769a96ad/image.png)

#### 한글 경로 쓰지 말자.

</details>

### LV.4-2 테스트 코드2 : 예상대로 예외처리 하는가?

<details>
<summary> 열기/닫기 </summary
					
### LV.4-2-1 목록 조회 시 TODO 없으면 에러
![](https://velog.velcdn.com/images/parslime/post/b9944dc6-a357-4fd2-b5a8-192705193a99/image.png)

실행
![](https://velog.velcdn.com/images/parslime/post/3580077e-82d3-471f-bda3-88a062528086/image.png)

필요값과 실제값이 다르다.

필요값은 
![](https://velog.velcdn.com/images/parslime/post/f12eaa20-62e8-4ab7-b3c9-9beba7438d69/image.png)

- 여기에 있는 Todo not found이므로 
assertEquals("이거랑", [이거랑]) 같아야 하므로 "Manager"만 바꾸면 된다.

![](https://velog.velcdn.com/images/parslime/post/548d64ad-e81c-45fe-a0ac-27fcc56f3252/image.png)


### LV.4-2-2 할 일을 찾지 못하면 에러남

![](https://velog.velcdn.com/images/parslime/post/4903a4fe-c07d-42e5-affe-fb130a3799b4/image.png)

실행

![](https://velog.velcdn.com/images/parslime/post/c3ffc183-aa35-490d-b268-ff2d66ed9d0c/image.png)

- ServerException이 아니라 InvalidRequest이다.
따라서

![](https://velog.velcdn.com/images/parslime/post/397efa10-99db-4d2b-a0de-329ffacba629/image.png)

![](https://velog.velcdn.com/images/parslime/post/d6515547-429b-46d9-b018-0fc5683c8eff/image.png)

### LV.4-2-3 일정 생성 없이 매너지 배정할 경우 에러
#### 상황 : 팀원이 잘 되던 로직을 수정해서 망가트렸을 때

> 테스트 패키지 org.example.expert.domain.manager.service의 ManagerServiceTest 클래스에 있는 todo의_user가_null인_경우_예외가_발생한다() 테스트가 성공할 수 있도록 
**[ 서비스 로직 ]**을 수정해 주세요.

![](https://velog.velcdn.com/images/parslime/post/2713b9be-5f44-4e97-a1bc-c0f16a20d8f7/image.png)

실행

![](https://velog.velcdn.com/images/parslime/post/8ee0a79d-9470-4415-8d91-feb09caa1f77/image.png)

InvalidRequestException이 필요한데, 
실제로는 NullPointerException이 나왔다.

+ 문제에서도 나와있다. 서비스 로직을 수정해보자.(똑바로 읽기)

![](https://velog.velcdn.com/images/parslime/post/df323031-e7b7-4063-b906-36f5a13511f4/image.png)

- InvalidRequestException : 잘못된 요청
- NullPointerException : 존재하지 않는 메모리 접근

즉 객체는 존재하지만 내용물이 존재하지 않는 메모리에 접근한 것들이 문제이다.
그렇기에 .getId()를 빼보자

![](https://velog.velcdn.com/images/parslime/post/19cc48c1-ce6e-4b21-bdcc-eb0082258cee/image.png)


![](https://velog.velcdn.com/images/parslime/post/7d37f4eb-6fb6-488e-8aa3-31aac5828001/image.png)

</details>

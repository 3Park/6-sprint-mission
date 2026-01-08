## 요구사항

### 기본

# JWT 컴포넌트 구현

## 1. JWT 의존성 추가

- [ ] 아래 의존성을 `build.gradle` 파일에 추가하세요.

```gradle
implementation 'com.nimbusds:nimbus-jose-jwt:10.3'
```

- [ ] 토큰을 발급, 갱신, 유효성 검사를 담당하는 컴포넌트(JwtTokenProvider)를 구현하세요.

# 리팩토링 - 로그인

미션 9와 마찬가지로 Spring Security의 formLogin + 미션 9의 인증 흐름은 그대로 유지하면서 필요한 부분만 대체합니다.

- [ ]  세션 생성 정책을 STATELESS로 변경하고, sessionConcurrency 설정을 삭제하세요.

  ```java
  http
        .sessionManagement(session ->session
          ...
                  .

sessionCreationPolicy(...)
      )
  ```

- [ ]  AuthenticationSuccessHandler 컴포넌트를 대체하세요.

기존 구현체는 LoginSuccessHandler입니다.

JwtLoginSuccessHandler를 정의하고 대체하세요.

   ```java

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
       ...

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
           ...
    }
}
   ```

인증 성공 시 JwtProvider를 활용해 토큰을 발급하세요.

엑세스 토큰은 응답 Body에 포함하세요.  
리프레시 토큰은 쿠키(REFRESH_TOKEN)에 저장하세요.

200 JwtDto로 응답합니다.

- [ ]  설정에 추가하세요.

  ```java
  http
        .formLogin(login ->login
          ...
                  .

successHandler(jwtLoginSuccessHandler)
      )
  ```

아래는 주어진 "JWT 인증 필터 구현" 관련 레슨의 모든 텍스트를 하나도 빠짐없이 담은 마크다운 문서예요.

# JWT 인증 필터 구현

JWT 인증 필터 구현

- [ ]  엑세스 토큰을 통해 인증하는 필터(JwtAuthenticationFilter)를 구현하세요.

```java
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {...}
```

- 요청 당 한번만 실행되도록 OncePerRequestFilter를 상속하세요.

- 요청 헤더(Authorization)에 Bearer 토큰이 포함된 경우에만 인증을 시도하세요.

- JwtProvider를 통해 엑세스 토큰의 유효성을 검사하세요.

- 유효한 토큰인 경우 UsernamePasswordAuthenticationToken 객체를 활용해 인증 완료 처리하세요.

```java
UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
SecurityContextHolder.

getContext().

setAuthentication(authentication);
```

# 리프레시 토큰을 활용한 엑세스 토큰 재발급

리프레시 토큰을 활용한 엑세스 토큰 재발급

- [ ]  리프레시 토큰을 활용해 엑세스 토큰을 재발급하는 API를 구현하세요.

## API 스펙

- 엔드포인트: POST /api/auth/refresh
- 요청: Header Cookie: REFRESH_TOKEN=…
- 응답
    - 리프레시 토큰이 유효한 경우: 200 JwtDto
    - 리프레시 토큰이 유효하지 않은 경우: 401 ErrorResponse

permitAll 설정에 포함하세요.

이 API는 엑세스 토큰이 없거나 만료된 상태에서 호출하게 됩니다.

- [ ]  리프레시 토큰 Rotation을 통해 보안을 강화하세요.

- [ ]  토큰 재발급 API로 대체할 수 있는 컴포넌트를 모두 삭제하세요.

- Me API (GET /auth/me)

- RememberMe

    - 쿠키에 저장된 리프레시 토큰이 RememberMe의 기능을 대체할 수 있습니다.

## 리팩토링 - 로그아웃

- [ ]  쿠키에 저장된 리프레시 토큰을 삭제하는 LogoutHandler를 구현하세요.

```java
public class JwtLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {...}
```

- [ ]  구현한 핸들러를 추가하세요.

```java
http
        .logout(logout ->logout
...
        .

addLogoutHandler(jwtLogoutHandler)
)
```

### 심화

# 리팩토링 - 토큰 상태 관리

토큰 기반 인증 방식은 세션 기반 인증 방식과 달리 **무상태(stateless)**이기 때문에 사용자의 로그인 상태를 제어하기 어렵습니다.

따라서 `SessionRegistry`를 통해 세션의 상태를 관리했던 것처럼, JWT의 상태를 관리할 수 있는 컴포넌트를 추가해야 합니다.

---

## JwtRegistry

- **registerJwtInformation**
    - 로그인 성공 시 `JwtInformation`을 등록합니다.
    - 최대 동시 로그인 수(1)를 제어합니다.
- **invalidateJwtInformationByUserId**
    - UserId로 해당 유저의 모든 `JwtInformation` 정보를 삭제합니다.
- **hasActiveJwtInformationBy\***
    - `JwtInformation`이 Registry에 존재하는지 확인합니다.
        - **ByUserId**: 사용자의 로그인 상태를 판단할 때 활용합니다.
        - **ByAccessToken**: 필터에서 유효한 토큰인지 확인할 때 활용합니다.
        - **ByRefreshToken**: 토큰 재발급 시 유효한 토큰인지 확인할 때 활용합니다.
- **rotateJwtInformation**
    - 토큰 재발급 시 토큰 로테이션을 수행합니다.
- **clearExpiredJwtInformation**
    - 만료된 `JwtInformation`을 삭제합니다.

---

## InMemoryJwtRegistry

메모리에 `JwtInformation`을 저장하는 `JwtRegistry` 구현체입니다.

동시성 처리를 위해 다음과 같이 구성하세요.

```java
public class InMemoryJwtRegistry implements JwtRegistry {
    // <userId, Queue<JwtInformation>>
    private final Map<UUID, Queue<JwtInformation>> origin = new ConcurrentHashMap<>();
    private final int maxActiveJwtCount;
  ...
}
```

---

## 리팩토링 및 구현 체크리스트

- [ ] 토큰의 상태를 관리하는 JwtRegistry를 구현하세요.
- [ ] JwtAuthenticationFilter에서 JwtRegistry를 활용해 토큰의 상태를 검사하는 로직을 추가하세요.
- [ ] JwtRegistry를 활용해 동시 로그인 제한 기능을 리팩토링하세요.
    - 동일한 계정으로 로그인 시 기존 로그인 세션을 무효화합니다.
- [ ] JwtRegistry를 활용해 권한이 변경된 사용자가 로그인 상태라면 강제로 로그아웃되도록 하세요.
- [ ] JwtRegistry를 활용해 사용자의 로그인 여부를 판단하도록 리팩토링하세요.
- [ ] JwtLogoutHandler에서 JwtRegistry를 활용해 로그아웃 시 토큰을 무효화하세요.

```java

@Override
public void logout(HttpServletRequest request, HttpServletResponse response,
                   Authentication authentication) {
  ...
    Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals(JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME))
            .findFirst()
            .ifPresent(cookie -> {...});
  ...
}
```

- 로그아웃 API는 인증이 필요없기 때문에 `Authentication` 정보가 없을 수 있습니다.
- 따라서 요청 쿠키의 리프레시 토큰을 활용해 토큰을 무효화합니다.

---

- [ ] 주기적으로 만료된 토큰 정보를 레지스트리에서 삭제하세요.

### @EnableScheduling를 추가하세요

```java

@Configuration
@EnableJpaAuditing
@EnableScheduling
public class AppConfig {
}
```

## 주요 변경사항

- 기본 요구사항 내용 반영
- 심화 요구사항 내용 반영

## 스크린샷

## 멘토에게

- 요구사항에 맞게 구현했는지는 모르겠습니다.
-
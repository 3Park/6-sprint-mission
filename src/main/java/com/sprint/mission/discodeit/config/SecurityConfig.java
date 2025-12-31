package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.handler.CustomAccessDeniedHandler;
import com.sprint.mission.discodeit.handler.HttpStatusReturningLogoutSuccessHandler;
import com.sprint.mission.discodeit.handler.LoginFailureHandler;
import com.sprint.mission.discodeit.handler.LoginSuccessHandler;
import com.sprint.mission.discodeit.handler.SpaCsrfTokenRequestHandler;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

  private final LoginFailureHandler loginFailureHandler;
  private final LoginSuccessHandler loginSuccessHandler;
  private final SpaCsrfTokenRequestHandler spaCsrfTokenRequestHandler;
  private final HttpStatusReturningLogoutSuccessHandler httpStatusReturningLogoutSuccessHandler;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(spaCsrfTokenRequestHandler))
        .authorizeHttpRequests(authorizeRequests ->
            authorizeRequests
                .requestMatchers(
                    "/index.html",
                    "/assets/**",
                    "favicon.ico",
                    "/api/auth/login",
                    "/api/auth/csrf-token",
                    "/api/auth/logout",
                    "/actuator/health",
                    "/actuator/info").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                .requestMatchers("/actuator/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        ).formLogin(x -> x
            .loginProcessingUrl("/api/auth/login")
            .successHandler(loginSuccessHandler)
            .failureHandler(loginFailureHandler)
            .permitAll()
        ).logout(x -> x
            .logoutUrl("/api/auth/logout")
            .logoutSuccessHandler(httpStatusReturningLogoutSuccessHandler)
            .permitAll()
        ).exceptionHandling(ex -> ex
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.FORBIDDEN))
            .accessDeniedHandler(customAccessDeniedHandler))
        .sessionManagement(management -> management
            .sessionConcurrency(c -> c
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry()))
        ).rememberMe(r -> r
            .key("remember-me")
            .rememberMeParameter("remember-me")
            .tokenValiditySeconds(60 * 60 * 24 * 7)
            .rememberMeCookieName("remember-me")
        );

    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring()
        .requestMatchers(
            "/index.html",
            "/assets/**",
            "favicon.ico",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html");
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CommandLineRunner commandLineRunner(UserRepository userRepository,
      PasswordEncoder passwordEncoder) {
    return args -> {
      if (userRepository.existsByRole(Role.ADMIN)) {
        return;
      }

      User user = User.builder()
          .username("admin")
          .password(passwordEncoder.encode("admin"))
          .email("admin@test.com")
          .profile(null)
          .build();

      user.updateRole(Role.ADMIN);
      userRepository.save(user);
    };
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_CHANNEL_MANAGER >ROLE_USER");
    return roleHierarchy;
  }

  @Bean
  static MethodSecurityExpressionHandler methodSecurityExpressionHandler(
      RoleHierarchy roleHierarchy) {
    DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
    handler.setRoleHierarchy(roleHierarchy);
    return handler;
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Bean
  public HttpSessionEventPublisher httpSessionEventPublisher() {
    return new HttpSessionEventPublisher();
  }
}

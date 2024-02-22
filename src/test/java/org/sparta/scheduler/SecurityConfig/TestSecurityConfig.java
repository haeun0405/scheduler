package org.sparta.scheduler.SecurityConfig;

import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());// CSRF 보호 기능을 비활성화

        http.sessionManagement(session -> session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS)) // 세션 기반 인증 비활성화
            .authorizeHttpRequests((authorizeHttpRequests) ->
                    authorizeHttpRequests
                        .requestMatchers(
                            String.valueOf(PathRequest.toStaticResources().atCommonLocations()))
                        .permitAll() // resources 접근 허용 설정
                        .requestMatchers("/api/auth/**").permitAll() // '/api/user/'로 시작하는 요청 모두 접근 허가
                       .requestMatchers(HttpMethod.DELETE, "/api/tasks/*/comments/*").authenticated()// '/api/comments/'로 시작하는 요청은 인증된 사용자만 접근 허가
                        .requestMatchers("/api/tasks/**")
                        .permitAll()// '/api/tasks/'로 시작하는 요청은 인증된 사용자만 접근 허가
                        .anyRequest().authenticated()
            );// 그 외 모든 요청 인증처리
        return http.build();
    }
}

package org.sparta.scheduler.Config;

import org.sparta.scheduler.Filter.JwtAuthenticationFilter;
import org.sparta.scheduler.Util.JwtUtil;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtUtil jwtUtil,UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable) // CSRF 보호 기능을 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 기반 인증 비활성화
                .authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(String.valueOf(PathRequest.toStaticResources().atCommonLocations())).permitAll() // resources 접근 허용 설정
                        .requestMatchers("/api/auth/**").permitAll() // '/api/user/'로 시작하는 요청 모두 접근 허가
                        .requestMatchers(HttpMethod.DELETE, "/api/tasks/**/comments/**").authenticated()// '/api/comments/'로 시작하는 요청은 인증된 사용자만 접근 허가
                        .requestMatchers("/api/tasks/**").authenticated()// '/api/tasks/'로 시작하는 요청은 인증된 사용자만 접근 허가
                        .anyRequest().authenticated()
                )// 그 외 모든 요청 인증처리
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class); // JWT 검증 필터 추가
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
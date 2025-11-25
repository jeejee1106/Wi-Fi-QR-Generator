package com.example.project.common.config;

import com.example.project.common.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception
    {
        http
            .csrf(csrf -> csrf.disable()) // 일단 개발 단계에선 끔
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/", "/user/login","/qr/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/user").permitAll()   // POST /user 허용
                    .requestMatchers(HttpMethod.GET, "/user").authenticated() // GET /user 인증 필요
                    .anyRequest().authenticated()
            )
            // JWT 필터 등록 부분!!
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            //UsernamePasswordAuthenticationFilter보다 jwtAuthenticationFilter가 꼭 앞에 있어야함.
            // why? 로그인 과정이 아니어도 요청마다 토큰을 먼저 검사해서SecurityContextHolder 에 인증을 넣어줄 수 있도록.

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();  // 자동으로 UserDetailsService + PasswordEncoder 조합
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

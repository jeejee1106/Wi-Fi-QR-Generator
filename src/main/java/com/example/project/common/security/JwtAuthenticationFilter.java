package com.example.project.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Authorization 헤더에서 토큰 가져오기
        String authHeader = request.getHeader("Authorization");

        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // "Bearer " 이후 문자열
        }

        // 토큰이 있고, 아직 SecurityContext 에 인증이 안 되어 있음
        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 2. 토큰 유효성 검사
            if (jwtTokenProvider.validateToken(jwt)) {

                // 3. 토큰에서 email(=username) 꺼내기
                String email = jwtTokenProvider.getEmail(jwt);

                // 4. email 로 DB에서 UserDetails 조회
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                // 5. 스프링 시큐리티 인증 객체 만들기
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 6. SecurityContext 에 인증 정보 저장
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 다음 필터 진행
        filterChain.doFilter(request, response);
    }
}

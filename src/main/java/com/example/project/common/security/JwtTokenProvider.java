package com.example.project.common.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // application.yml 에서 주입받을 값들
    @Value("${jwt.secret}")
    private String secretKeyPlain;

    @Value("${jwt.expiration-ms:3600000}") // 기본 1시간
    private long tokenValidityInMs;

    private Key key;

    // secretKeyPlain 문자열을 실제 서명용 Key 객체로 변환
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKeyPlain.getBytes());
    }

    /**
     * 로그인 성공 후 Authentication 객체를 받아서 JWT 생성
     */
    public String createToken(Authentication authentication) {
        String email = authentication.getName(); // username == email
        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMs);

        return Jwts.builder()
                .setSubject(email)       // 토큰의 주체 (우리는 email로 사용)
                .setIssuedAt(now)        // 발급 시간
                .setExpiration(validity) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰에서 email(subject) 추출
     */
    public String getEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 토큰 유효성 검증 (형식, 서명, 만료시간 체크)
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 서명 불일치, 만료, 변조된 토큰 등등
            return false;
        }
    }

}

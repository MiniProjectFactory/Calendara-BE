package com.dev.calendara.config.security;

import com.dev.calendara.member.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final MemberService memberService;

    @Value("${jwt.custom.secret}")
    private String secretKey;
    @Value("${jwt.custom.token.valid.milliseconds}")
    private final long tokenValidMs;

    @PostConstruct
    protected void init() {
        logger.info("[init] JwtTokenProvider secretKey 초기화 시작");
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        logger.info("[init] JwtTokenProvider secretKey 초기화 완료");
    }

    public String createToken(String email) {
        logger.info("[createToken] JwtTokenProvider 토큰생성 시작");
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        logger.info("[createToken] JwtTokenProvider 토큰생성 완료");
        return token;
    }

    public Authentication getAuthentication(String token) {
        logger.info("[getAuthentication] JwtTokenProvider 토큰 인증 정보 조회 시작");
        UserDetails userDetails = memberService.loadUserByUsername(this.getUsername(token));
        logger.info("[getAuthentication] JwtTokenProvider 토큰 인증 정보 조회 완료, UserDetails UserName : {}",
                userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        logger.info("[getUsername] JwtTokenProvider 토큰 기반 회원 구별 정보 추출");
        String info = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody()
                .getSubject();
        logger.info("[getUsername] JwtTokenProvider 토큰 기반 회원 구별 정보 추출 완료, info : {}", info);
        return info;
    }

    public String resolveToken(HttpServletRequest request) {
        logger.info("[resolveToken] JwtTokenProvider HTTP 헤더에서 Token 값 추출");
        return request.getHeader("X-AUTH-TOKEN");
    }

    // 유효기간 체크
    public boolean validateToken(String token) {
        logger.info("[validateToken] JwtTokenProvider 토큰 유효 체크 시작");
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            logger.info("[validateToken] JwtTokenProvider 토큰 유효 체크 완료");
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            logger.info("[validateToken] JwtTokenProvider 토큰 유효 체크 예외 발생");
            return false;
        }
    }
}

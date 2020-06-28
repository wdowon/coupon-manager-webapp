package com.jaeholee.coupon.member.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenCreater {

    private long tokenValidMillisecond = 1000L * 60 * 60; // 만료시간 1시간

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private SecretKey key;

    /**
     * Init.
     */
    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(Encoders.BASE64URL.encode(secretKey.getBytes()).getBytes());
    }

    /**
     * Create token string.
     * 토큰 생성
     *
     * @param userId the user id
     * @return the string
     */
    public String createToken(String userId) {
        Claims claims = Jwts.claims().setSubject(userId); //토큰 정보 내 userId 추가

        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setIssuedAt(now)   // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public SecretKey getKey() {
        return key;
    }
}

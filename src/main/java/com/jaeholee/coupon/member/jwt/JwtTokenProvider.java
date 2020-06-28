package com.jaeholee.coupon.member.jwt;

import com.jaeholee.coupon.member.domain.Member;
import com.jaeholee.coupon.member.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

/**
 * The type Jwt token provider.
 * JWT 토큰 생성 및 검증
 * Author : wdowon@gmail.com
 */
@Component
public class JwtTokenProvider {

    private final String BEARER_TYPE = "Bearer";

    private final MemberService memberService;
    private final JwtTokenCreater jwtTokenCreater;

    @Autowired
    public JwtTokenProvider(MemberService memberService, JwtTokenCreater jwtTokenCreater) {
        this.memberService = memberService;
        this.jwtTokenCreater = jwtTokenCreater;
    }

    /**
     * Gets authentication.
     * JWT 토큰으로 인증 정보 조회
     *
     * @param token the token
     * @return the authentication
     */
    public Authentication getAuthentication(String token) {
        Member member = memberService.getMember(getUserId(token));
        return new JwtAuthToken(member.getUserId(), member.getUserPw(), createAuthorityList("ROLE_USER"));
    }

    /**
     * Gets user id.
     * 토큰 내 USERID 추출
     *
     * @param token the token
     * @return the user id
     */
    public String getUserId(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(jwtTokenCreater.getKey()).build();
        return parser.parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Resolve token string.
     * 토큰 파싱
     *
     * @param req the req
     * @return the string
     * @throws AuthenticationException the authentication exception
     */
    public String resolveToken(HttpServletRequest req) throws AuthenticationException {
        String authHeader = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) {
            return null;
        }
        String[] authHeaders = authHeader.split(" ");
        if (authHeaders.length != 2) {
            return null;
        }
        if (!authHeaders[0].equals(BEARER_TYPE)) {
            return null;
        }
        return authHeaders[1];
    }

    /**
     * Validate token boolean.
     * 토큰 유효성 확인
     *
     * @param jwtToken the jwt token
     * @return the boolean
     */
    public boolean validateToken(String jwtToken) {
        try {
            JwtParser parser = Jwts.parserBuilder().setSigningKey(jwtTokenCreater.getKey()).build();
            Jws<Claims> claims = parser.parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}

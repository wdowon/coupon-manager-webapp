package com.jaeholee.coupon.member.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * The type Jwt authentication filter.
 * jwt 토큰 인증 필터
 * Author : wdowon@gmail.com
 */
public class JwtAuthenticationFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    private JwtTokenProvider jwtTokenProvider;

    /**
     * Instantiates a new Jwt authentication filter.
     *
     * @param jwtTokenProvider the jwt token provider
     */
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            try {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                //인증정보 각 호출 시마다 확인 후 객체 심어줌.
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                logger.error("[JwtAuthenticationFilter] e : {}", e.toString());
            }
        }
        filterChain.doFilter(request, response);
    }
}

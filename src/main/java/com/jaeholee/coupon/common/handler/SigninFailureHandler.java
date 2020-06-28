package com.jaeholee.coupon.common.handler;

import com.jaeholee.coupon.common.error.CommonResultCode;
import com.jaeholee.coupon.common.error.CommonResultResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Signin failure handler.
 * Spring Security 인증 실패 핸들러
 * Author : wdowon@gmail.com
 */
@Component
public class SigninFailureHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(SigninFailureHandler.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        logger.error("[SigninFailureHandler] AuthenticationEntryPoint e : {}", ex.toString());

        //인증 실패 응답 리턴
        CommonResultResponse res = new CommonResultResponse(CommonResultCode.SIGNIN_FAILURE);

        response.setContentType("application/json");
        response.getOutputStream()
                .println(objectMapper.writeValueAsString(res));
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        logger.error("[SigninFailureHandler] AccessDeniedHandler e : {}", e.toString());

        //인증 실패 응답 리턴
        CommonResultResponse res = new CommonResultResponse(CommonResultCode.SIGNIN_FAILURE);

        response.setContentType("application/json");
        response.getOutputStream()
                .println(objectMapper.writeValueAsString(res));
    }
}

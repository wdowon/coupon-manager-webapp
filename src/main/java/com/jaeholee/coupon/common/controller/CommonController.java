package com.jaeholee.coupon.common.controller;

import com.jaeholee.coupon.common.error.CommonResultResponse;
import com.jaeholee.coupon.common.exception.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 공통 Exception Handler controller.
 * Author : wdowon@gmail.com
 */
@RestControllerAdvice
public class CommonController {

    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    /**
     * 커스텀 정의된 exception 발생 시 응답 제공 및 로깅
     *
     * @param e the CommonException e
     * @return the common result response
     */
    @ExceptionHandler(CommonException.class)
    public CommonResultResponse commonError(CommonException e) {
        logger.info("commonException {}, cause {}", e.toString(), e.getCause());

        CommonResultResponse errResponse = new CommonResultResponse(e);
        return errResponse;
    }

    /**
     * 그 외 exception 발생 시 에러 로깅
     *
     * @param e the e
     */
    @ExceptionHandler(Exception.class)
    public CommonResultResponse exceptionError(Exception e) {
        logger.error("Exception {}, cause {}", e.toString(), e.getCause());

        CommonResultResponse errResponse = new CommonResultResponse(e);
        return errResponse;
    }
}

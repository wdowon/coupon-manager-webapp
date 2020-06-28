package com.jaeholee.coupon.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * The type Async exception handler.
 * Author : wdowon@gmail.com
 */
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(AsyncExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
        logger.error("[AsyncExceptionHandler] e : {}", throwable.toString());
    }
}

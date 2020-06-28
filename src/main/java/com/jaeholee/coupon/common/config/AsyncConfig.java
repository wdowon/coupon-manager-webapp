package com.jaeholee.coupon.common.config;

import com.jaeholee.coupon.common.handler.AsyncExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * The type Async config.
 * 쿠폰 생성 시 사용을 위한 Async config.
 * Author : wdowon@gmail.com
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    //기본 Thread 수
    private static int TASK_CORE_POOL_SIZE = 2;
    //최대 Thread 수
    private static int TASK_MAX_POOL_SIZE = 4;
    //Queue 수
    private static int TASK_QUEUE_CAPACITY = 10;

    @Bean
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(TASK_CORE_POOL_SIZE);
        executor.setMaxPoolSize(TASK_MAX_POOL_SIZE);
        executor.setQueueCapacity(TASK_QUEUE_CAPACITY);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }
}

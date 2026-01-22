package com.sprint.mission.discodeit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("Async method exception occurred!");
        log.error("Method name: {}", method.getName());
        log.error("Params: {}", Arrays.toString(params));
        log.error("Exception message: {}", ex.getMessage(), ex);
    }
}

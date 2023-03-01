package com.example.demo.config.advice;

import com.example.demo.entity.LogEntity;
import com.example.demo.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingAdvice {

    private final LogService logService;

    @Pointcut(value = "execution(* com.example.demo.controller.*.*(..))")
    public void generalPointcut(){}

    @Around("generalPointcut()")
    public Object appLogging(ProceedingJoinPoint pjp) {
        String method = pjp.getSignature().getName();
        String className = pjp.getTarget().getClass().toString();
        Object[] array = pjp.getArgs();
        String person = SecurityContextHolder.getContext().getAuthentication().getName();

        LogEntity log = new LogEntity();
        log.setCreateDttm(LocalDateTime.now());
        log.setLog("Класс " + className + ", метод " + method + " (" + Arrays.toString(array) + "). Вызвал " + person);

        logService.saveLog(log);

        Object object;

        try {
            object = pjp.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        return object;
    }
}

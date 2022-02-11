package com.emulator.atmservice.annotation;

import com.emulator.atmservice.service.impl.AuthorizationTokenDb;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
 
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CheckAuthorizationTokenAspect {
    private final AuthorizationTokenDb authorizationTokenDb;
 
    @Around("@annotation(CheckToken)")
    public Object ValidateToken(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Checking token validity to perform requested method");
        String token =  joinPoint.getArgs()[0].toString();
        authorizationTokenDb.verifyGivenToken(token);
        log.info("Token validity has been successfully checked");

        return joinPoint.proceed();
    }
 
}
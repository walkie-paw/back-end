package com.WalkiePaw.global.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.UUID;

@Slf4j
@Aspect
public class TraceAspect {

    /**
     * @Trace 붙은 메소드 시간 측정
     */
    @Around("@annotation(com.WalkiePaw.global.aspect.annotation.Trace)")
    public void doTrace(ProceedingJoinPoint joinPoint) throws Throwable {
        String uuid = UUID.randomUUID().toString().substring(0, 7);
        log.debug("[{}] {} start", uuid, joinPoint.getSignature());
        long startTime = System.currentTimeMillis();
        joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.debug("[{}] {} end - {}ms", uuid, joinPoint.getSignature(), endTime - startTime);
    }
}

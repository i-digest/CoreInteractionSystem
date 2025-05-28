package com.coreintegration.monitoring.aspect;

import com.coreintegration.monitoring.metrics.RequestMetricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MonitoringAspect {

    private final RequestMetricsService requestMetricsService;

    @Around("execution(* com.coreintegration..*(..)) && !execution(* com.coreintegration.monitoring..*(..))")
    public Object logInboundApiCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        final Signature signature = joinPoint.getSignature();
        requestMetricsService.countRequest(signature.getName());
        try {
            Object result = joinPoint.proceed();
            long elapsed = System.currentTimeMillis() - start;
            log.debug("[{}] executed in {} ms", signature, elapsed);
            return result;
        } catch (Throwable t) {
            log.error("[{}] failed: {}", signature, t.getMessage(), t);
            throw t;
        }
    }
}

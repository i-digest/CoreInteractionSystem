package com.coreintegration.deduplication.aspect;

import com.coreintegration.deduplication.annotation.Idempotent;
import com.coreintegration.deduplication.storage.InMemoryIdempotencyStorage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class IdempotencyAspect {

    private final InMemoryIdempotencyStorage storage;

    @Around("@annotation(idempotent)")
    public Object handleIdempotency(ProceedingJoinPoint pjp, Idempotent idempotent) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String key = request.getHeader(idempotent.headerKey());

        if (key == null || key.isBlank()) {
            return pjp.proceed();
        }

        Optional<Object> cached = storage.get(key);
        if (cached.isPresent()) {
            return cached.get();
        }

        Object result = pjp.proceed();
        storage.put(key, result);
        return result;
    }
}

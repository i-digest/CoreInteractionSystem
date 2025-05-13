package com.coreintegration.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class CircuitBreakerExecutor {

    private final CircuitBreakerRegistry registry;

    public <T> T run(String name, Supplier<T> supplier, Supplier<T> fallback) {
        CircuitBreaker cb = registry.circuitBreaker(name);
        try {
            return CircuitBreaker.decorateSupplier(cb, supplier).get();
        } catch (Exception ex) {
            return fallback.get();
        }
    }
}

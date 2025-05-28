package com.coreintegration.circuitbreaker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CircuitBreakerExecutorIT {

    @Autowired
    private CircuitBreakerExecutor executor;


    @Test
    void shouldReturnFallbackWhenExceptionOccurs() {
        AtomicInteger fallbackCalled = new AtomicInteger();

        String result = executor.run("testCircuit",
            () -> { throw new RuntimeException("Simulated failure"); },
            () -> { fallbackCalled.incrementAndGet(); return "fallback"; }
        );

        assertEquals("fallback", result);
        assertEquals(1, fallbackCalled.get());
    }
}

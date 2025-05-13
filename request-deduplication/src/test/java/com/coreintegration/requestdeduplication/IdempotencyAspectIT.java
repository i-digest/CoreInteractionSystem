package com.coreintegration.requestdeduplication;

import com.coreintegration.deduplication.storage.InMemoryIdempotencyStorage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class IdempotencyAspectIT {

    @Autowired
    private InMemoryIdempotencyStorage storage;

    @Test
    void shouldReturnSameValueForSameKey() {
        storage.put("X-123", "result");
        Optional<Object> result = storage.get("X-123");
        assertEquals("result", result.orElse(null));
    }
}
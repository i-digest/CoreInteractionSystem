package com.coreintegration.deduplication.storage;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryIdempotencyStorage {

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    public Optional<Object> get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry != null && entry.expiry > Instant.now().toEpochMilli()) {
            return Optional.of(entry.value);
        }
        return Optional.empty();
    }

    public void put(String key, Object result) {
        cache.put(key, new CacheEntry(result, Instant.now().plusSeconds(600).toEpochMilli()));
    }

    private record CacheEntry(Object value, long expiry) {}
}

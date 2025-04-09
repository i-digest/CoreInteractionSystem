package com.coreintegration.inbound;

import com.coreintegration.caching.service.CacheServiceAware;
import com.coreintegration.fallbackengine.service.FallBackServiceAware;
import com.coreintegration.ratelimiter.service.RateLimiterServiceAware;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExecutionPolicyEngine {

    private final RateLimiterServiceAware rateLimiterServiceAware;
    private final CacheServiceAware cacheServiceAware;
    private final FallBackServiceAware fallBackServiceAware;

    public <O> O run(String limiterName, String key, Class<O> returnType) {
        return null;
    }
}

package com.coreintegration.inbound.service;

import com.coreintegration.commons.model.LimitsListResponse;
import com.coreintegration.commons.model.LimitsResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LimitsService {

    @RateLimiter(name = "limitsRateLimiter")
    public LimitsResponse getById(String id) {
        return null;
    }

    @RateLimiter(name = "limitsBulkRateLimiter")
    public LimitsListResponse getByIds(List<String> ids) {
        return null;
    }
}

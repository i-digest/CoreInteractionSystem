package com.coreintegration.inbound.service;

import com.coreintegration.commons.model.LimitsListResponseDto;
import com.coreintegration.commons.model.LimitsResponseDto;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LimitsService {

    @RateLimiter(name = "limitsRateLimiter")
    public LimitsResponseDto getLimitsByAccountId(final UUID accountId) {
        return null;
    }

    @RateLimiter(name = "limitsBulkRateLimiter")
    public LimitsListResponseDto getLimitsByAccountIds(final List<UUID> accountIds) {
        return null;
    }
}

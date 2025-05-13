package com.coreintegration.inbound.service;

import com.coreintegration.commons.model.LegalEntitiesListResponseDto;
import com.coreintegration.commons.model.LegalEntitiesResponseDto;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LegalEntitiesService {

    @RateLimiter(name = "legalEntitiesRateLimiter")
    public LegalEntitiesResponseDto getLegalEntitiesByAccountId(final UUID accountId) {
        return null;
    }

    @RateLimiter(name = "legalEntitiesBulkRateLimiter")
    public LegalEntitiesListResponseDto getLegalEntitiesByAccountIds(final List<UUID> accountIds) {
        return null;
    }
}

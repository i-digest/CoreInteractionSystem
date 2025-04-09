package com.coreintegration.inbound.service;

import com.coreintegration.commons.model.LegalEntitiesListResponse;
import com.coreintegration.commons.model.LegalEntitiesResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LegalEntitiesService {

    @RateLimiter(name = "legalEntitiesRateLimiter")
    public LegalEntitiesResponse getById(String id) {
        return null;
    }

    @RateLimiter(name = "legalEntitiesBulkRateLimiter")
    public LegalEntitiesListResponse getByIds(List<String> ids) {
        return null;
    }
}

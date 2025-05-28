package com.coreintegration.inbound.service;

import com.coreintegration.commons.model.LegalEntitiesListResponseDto;
import com.coreintegration.commons.model.LegalEntitiesResponseDto;
import com.coreintegration.commons.model.LegalEntityDto;
import com.coreintegration.database.service.LegalEntitiesCachedDatabaseServiceAware;
import com.coreintegration.outbound.client.LegalEntitiesClient;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LegalEntitiesService {

    private final LegalEntitiesCachedDatabaseServiceAware cacheServiceAware;
    private final LegalEntitiesClient legalEntitiesClient;

    @RateLimiter(name = "legalEntitiesRateLimiter")
    public LegalEntitiesResponseDto getLegalEntitiesByAccountId(@NonNull final UUID accountId) {
        final List<UUID> idsToFetchFromCore = new ArrayList<>();
        final List<LegalEntityDto> legalEntities = cacheServiceAware.getLegalEntitiesByAccountId(accountId, idsToFetchFromCore, () -> legalEntitiesClient.getLegalEntitiesByAccountId(accountId));

        return new LegalEntitiesResponseDto().legalEntities(legalEntities);
    }

    @RateLimiter(name = "legalEntitiesBulkRateLimiter")
    public LegalEntitiesListResponseDto getLegalEntitiesByAccountIds(@NonNull final List<UUID> accountIds) {
        final List<UUID> idsToFetchFromCore = new ArrayList<>();
        final List<LegalEntityDto> legalEntities = cacheServiceAware.getLegalEntitiesByAccountIds(accountIds, idsToFetchFromCore, () -> legalEntitiesClient.getLegalEntitiesByAccountIds(idsToFetchFromCore));
        final Map<String, List<LegalEntityDto>> legalEntityMap = legalEntities.stream()
                .collect(Collectors.groupingBy(le -> le.getId().toString()));

        return new LegalEntitiesListResponseDto().legalEntities(legalEntityMap);
    }
}

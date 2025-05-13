package com.coreintegration.inbound.service;

import com.coreintegration.commons.model.LimitDto;
import com.coreintegration.commons.model.LimitsListResponseDto;
import com.coreintegration.commons.model.LimitsResponseDto;
import com.coreintegration.database.service.LimitsCachedDatabaseServiceAware;
import com.coreintegration.outbound.client.LimitsClient;
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
public class LimitsService {

    private final LimitsCachedDatabaseServiceAware cacheServiceAware;
    private final LimitsClient limitsClient;

    @RateLimiter(name = "limitsRateLimiter")
    public LimitsResponseDto getLimitsByAccountId(@NonNull final UUID accountId) {
        final List<UUID> idsToFetchFromCore = new ArrayList<>();
        final List<LimitDto> limits = cacheServiceAware.getLimitsByAccountId(accountId, idsToFetchFromCore, () -> limitsClient.getLimitsByAccountId(accountId));

        return new LimitsResponseDto().limits(limits);
    }

    @RateLimiter(name = "limitsBulkRateLimiter")
    public LimitsListResponseDto getLimitsByAccountIds(@NonNull final List<UUID> accountIds) {
        final List<UUID> idsToFetchFromCore = new ArrayList<>();
        final List<LimitDto> limits = cacheServiceAware.getLimitsByAccountIds(accountIds, idsToFetchFromCore, () -> limitsClient.getLimitsByAccountIds(idsToFetchFromCore));
        final Map<String, List<LimitDto>> limitsMap = limits.stream()
                .collect(Collectors.groupingBy(dc -> dc.getId().toString()));

        return new LimitsListResponseDto().limits(limitsMap);
    }
}

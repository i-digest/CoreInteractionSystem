package com.coreintegration.inbound.service;


import com.coreintegration.commons.model.BalanceDto;
import com.coreintegration.commons.model.BalanceListResponseDto;
import com.coreintegration.commons.model.BalanceResponseDto;
import com.coreintegration.database.service.BalanceCachedDatabaseServiceAware;
import com.coreintegration.outbound.client.BalanceClient;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BalancesService {

    private final BalanceCachedDatabaseServiceAware cacheServiceAware;
    private final BalanceClient balanceClient;

    @RateLimiter(name = "balancesRateLimiter")
    public BalanceResponseDto getBalanceById(@NonNull final UUID balanceId) {
        final List<UUID> idsToFetchFromCore = new ArrayList<>();
        List<BalanceDto> balances = cacheServiceAware.getBalance(balanceId, idsToFetchFromCore, () -> balanceClient.getBalanceFromCore(balanceId));

        return new BalanceResponseDto().balances(balances);
    }

    @RateLimiter(name = "balancesBulkRateLimiter")
    public BalanceListResponseDto getBalancesByIds(@NonNull final List<UUID> balanceIds) {
        final List<UUID> idsToFetchFromCore = new ArrayList<>();
        final Collection<BalanceDto> balances = cacheServiceAware.getListOfBalance(balanceIds, idsToFetchFromCore, () -> balanceClient.getListOfBalancesFromCore(idsToFetchFromCore));
        final Map<String, List<BalanceDto>> balancesMap = balances.stream()
                .collect(Collectors.groupingBy(dc -> dc.getId().toString()));

        return new BalanceListResponseDto().balances(balancesMap);
    }

}

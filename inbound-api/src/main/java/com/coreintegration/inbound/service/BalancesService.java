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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BalancesService {

    private final BalanceCachedDatabaseServiceAware cacheServiceAware;
    private final BalanceClient balanceClient;

    @RateLimiter(name = "balancesRateLimiter")
    public BalanceResponseDto getBalanceById(@NonNull final UUID balanceId) {
        BalanceDto balance = cacheServiceAware.getBalance(balanceId, () -> balanceClient.getBalanceFromCore(balanceId));

        return new BalanceResponseDto().balances(balance);
    }

    @RateLimiter(name = "balancesBulkRateLimiter")
    public BalanceListResponseDto getBalancesByIds(@NonNull final List<UUID> balanceIds) {
        final List<UUID> idsToFetchFromCore = new ArrayList<>();
        final List<BalanceDto> balances = cacheServiceAware.getListOfBalance(balanceIds, idsToFetchFromCore, () -> balanceClient.getListOfBalancesFromCore(idsToFetchFromCore));
        final Map<String, BalanceDto> balancesMap = balances.stream()
                .collect(Collectors.toMap(balance -> balance.getId().toString(), balance -> balance, (a, b) -> b));

        return new BalanceListResponseDto().balances(balancesMap);
    }

}

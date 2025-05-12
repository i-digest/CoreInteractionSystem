package com.coreintegration.inbound.service;

import com.coreintegration.commons.model.Balance;
import com.coreintegration.commons.model.BalanceListResponse;
import com.coreintegration.commons.model.BalanceResponse;
import com.coreintegration.database.service.BalanceCachedDatabaseServiceAware;
import com.coreintegration.outbound.client.BalanceClient;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BalancesService {

    private final BalanceCachedDatabaseServiceAware cacheServiceAware;
    private final BalanceClient balanceClient;

    @RateLimiter(name = "balancesRateLimiter")
    public BalanceResponse getBalanceById(@NonNull final String balanceId) {
        Balance balance = cacheServiceAware.getBalance(balanceId, () -> balanceClient.getBalanceFromCore(balanceId));

        return new BalanceResponse().balances(balance);
    }

    @RateLimiter(name = "balancesBulkRateLimiter")
    public BalanceListResponse getBalancesByIds(@NonNull final List<String> balanceIds) {
        final List<String> objects = new ArrayList<>();
        final List<Balance> balances = cacheServiceAware.getListOfBalance(balanceIds, objects, () -> balanceClient.getListOfBalancesFromCore(objects));
        final Map<String, Balance> accountDetailsMap =  balances.stream().collect(Collectors.toMap(Balance::getAccountId, Function.identity()));

        return new BalanceListResponse().balances(accountDetailsMap);
    }

}

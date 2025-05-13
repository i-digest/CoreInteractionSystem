package com.coreintegration.outbound.client;

import com.coreintegration.circuitbreaker.CircuitBreakerExecutor;
import com.coreintegration.commons.model.BalanceDto;
import com.coreintegration.fallbackengine.service.BalancesFallbackService;
import com.coreintegration.outbound.client.api.BalancesApi;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BalanceClient {

    private final BalancesApi api = new BalancesApi();
    private final CircuitBreakerExecutor circuitBreakerExecutor;
    private final BalancesFallbackService fallbackService;

    @NonNull
    public List<BalanceDto> getBalanceByAccountId(@NotBlank final UUID accountId) {
        return circuitBreakerExecutor.run("getBalancesByAccountIds",
                () -> api.getBalanceByAccountId(accountId).getBalances(),
                () -> fallbackService.getFallbackByAccountId(accountId));
    }

    @NonNull
    public Collection<BalanceDto> getBalancesByAccountIds(@NonNull final List<UUID> accountIds) {
        return circuitBreakerExecutor.run(
                        "getBalancesByAccountIds",
                        () -> api.getBalancesByAccountIds(accountIds).getBalances(),
                        () -> fallbackService.getFallbackByAccountIds(accountIds))
                .values()
                .stream()
                .flatMap(List::stream)
                .toList();
    }
}

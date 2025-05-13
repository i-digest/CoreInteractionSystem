package com.coreintegration.outbound.client;

import com.coreintegration.circuitbreaker.CircuitBreakerExecutor;
import com.coreintegration.commons.model.BalanceDto;
import com.coreintegration.database.mapper.BalanceMapper;
import com.coreintegration.database.repository.BalanceRepository;
import com.coreintegration.outbound.client.api.BalancesApi;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BalanceClient {

    private final BalancesApi api = new BalancesApi();
    private final CircuitBreakerExecutor circuitBreakerExecutor;
    private final BalanceRepository balanceRepository;
    private final BalanceMapper balanceMapper;

    @NonNull
    public List<BalanceDto> getBalanceByAccountId(@NotBlank final UUID accountId) {
        return circuitBreakerExecutor.run("getBalancesByAccountIds",
                () -> api.getBalanceByAccountId(accountId).getBalances(),
                () -> balanceRepository.findAllById(Collections.singleton(accountId)).stream()
                        .map(balanceMapper::toDto)
                        .toList());
    }

    @NonNull
    public Collection<BalanceDto> getBalancesByAccountIds(@NonNull final List<UUID> accountIds) {
        return circuitBreakerExecutor.run(
                        "getBalancesByAccountIds",
                        () -> api.getBalancesByAccountIds(accountIds).getBalances(),
                        () -> balanceRepository.findAllByAccountIdIn(accountIds).stream()
                                .map(balanceMapper::toDto)
                                .collect(Collectors.groupingBy(dc -> dc.getAccountId().toString()))
                ).values()
                .stream()
                .flatMap(List::stream)
                .toList();
    }
}

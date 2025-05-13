package com.coreintegration.outbound.client;

import com.coreintegration.commons.model.BalanceDto;
import com.coreintegration.outbound.client.api.BalancesApi;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class BalanceClient {

    private final BalancesApi api = new BalancesApi();

    @NonNull
    public List<BalanceDto> getBalanceFromCore(@NotBlank final UUID accountId) {
        return api.getBalanceByAccountId(accountId)
                .getBalances();
    }

    @NonNull
    public Collection<BalanceDto> getListOfBalancesFromCore(@NonNull final List<UUID> accountIds) {
        return api.getBalancesByAccountIds(accountIds)
                .getBalances()
                .values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}

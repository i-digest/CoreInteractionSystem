package com.coreintegration.outbound.client;

import com.coreintegration.commons.model.BalanceDto;
import com.coreintegration.commons.model.BalanceListResponseDto;
import com.coreintegration.commons.model.BalanceResponseDto;
import com.coreintegration.outbound.client.api.BalancesApi;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
public class BalanceClient {

    private final BalancesApi client = new BalancesApi();

    @NonNull
    public BalanceDto getBalanceFromCore(@NotBlank final UUID accountId) {
        final BalanceResponseDto responseDto = client.getBalanceByAccountId(accountId);
        return responseDto.getBalances();
    }

    @NonNull
    public Collection<BalanceDto> getListOfBalancesFromCore(@NonNull final List<UUID> accountIds) {
        final BalanceListResponseDto responseDto = client.getBalancesByAccountIds(accountIds);
        return responseDto.getBalances().values();
    }
}

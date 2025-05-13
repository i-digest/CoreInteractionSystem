package com.coreintegration.outbound.client;

import com.coreintegration.commons.model.LoanDto;
import com.coreintegration.outbound.client.api.LoansApi;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class LoansClient {

    private final LoansApi api = new LoansApi();

    @NonNull
    public List<LoanDto> getLimitsByAccountId(@NotBlank final UUID accountId) {
        return api.getLimitsByAccountId(accountId)
                .getLoans();
    }

    @NonNull
    public Collection<LoanDto> getLimitsByAccountIds(@NonNull final List<UUID> accountIds) {
        return api.getLimitsByAccountIds(accountIds)
                .getLoans()
                .values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}

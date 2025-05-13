package com.coreintegration.outbound.client;

import com.coreintegration.circuitbreaker.CircuitBreakerExecutor;
import com.coreintegration.commons.model.LoanDto;
import com.coreintegration.fallbackengine.service.LoansFallbackService;
import com.coreintegration.outbound.client.api.LoansApi;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoansClient {

    private final LoansApi api = new LoansApi();
    private final CircuitBreakerExecutor circuitBreakerExecutor;
    private final LoansFallbackService fallbackService;

    @NonNull
    public List<LoanDto> getLoansByAccountId(@NotBlank final UUID accountId) {
        return circuitBreakerExecutor.run("getLoansByAccountIds",
                () -> api.getLoansByAccountId(accountId).getLoans(),
                () -> fallbackService.getFallbackByAccountId(accountId));
    }

    @NonNull
    public Collection<LoanDto> getLoansByAccountIds(@NonNull final List<UUID> accountIds) {
        return circuitBreakerExecutor.run(
                        "getLoansByAccountIds",
                        () -> api.getLoansByAccountIds(accountIds).getLoans(),
                        () -> fallbackService.getFallbackByAccountIds(accountIds))
                .values()
                .stream()
                .flatMap(List::stream)
                .toList();
    }
}

package com.coreintegration.outbound.client;

import com.coreintegration.circuitbreaker.CircuitBreakerExecutor;
import com.coreintegration.commons.model.LoanDto;
import com.coreintegration.database.mapper.LoansMapper;
import com.coreintegration.database.repository.LoansRepository;
import com.coreintegration.outbound.client.api.LoansApi;
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
public class LoansClient {

    private final LoansApi api = new LoansApi();
    private final CircuitBreakerExecutor circuitBreakerExecutor;
    private final LoansRepository loansRepository;
    private final LoansMapper loansMapper;

    @NonNull
    public List<LoanDto> getLoansByAccountId(@NotBlank final UUID accountId) {
        return circuitBreakerExecutor.run("getLoansByAccountIds",
                () -> api.getLoansByAccountId(accountId).getLoans(),
                () -> loansRepository.findAllById(Collections.singleton(accountId)).stream()
                        .map(loansMapper::toDto)
                        .toList());
    }

    @NonNull
    public Collection<LoanDto> getLoansByAccountIds(@NonNull final List<UUID> accountIds) {
        return circuitBreakerExecutor.run(
                        "getLoansByAccountIds",
                        () -> api.getLoansByAccountIds(accountIds).getLoans(),
                        () -> loansRepository.findAllByAccountIdIn(accountIds).stream()
                                .map(loansMapper::toDto)
                                .collect(Collectors.groupingBy(dc -> dc.getAccountId().toString()))
                ).values()
                .stream()
                .flatMap(List::stream)
                .toList();
    }
}

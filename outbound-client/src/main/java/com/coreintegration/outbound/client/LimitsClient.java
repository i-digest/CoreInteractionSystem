package com.coreintegration.outbound.client;

import com.coreintegration.circuitbreaker.CircuitBreakerExecutor;
import com.coreintegration.commons.model.LimitDto;
import com.coreintegration.database.mapper.LimitsMapper;
import com.coreintegration.database.repository.LimitsRepository;
import com.coreintegration.outbound.client.api.LimitsApi;
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
public class LimitsClient {

    private final LimitsApi api = new LimitsApi();
    private final CircuitBreakerExecutor circuitBreakerExecutor;
    private final LimitsRepository limitsRepository;
    private final LimitsMapper limitsMapper;

    @NonNull
    public List<LimitDto> getLimitsByAccountId(@NotBlank final UUID accountId) {
        return circuitBreakerExecutor.run("getLimitsByAccountIds",
                () -> api.getLimitsByAccountId(accountId).getLimits(),
                () -> limitsRepository.findAllById(Collections.singleton(accountId)).stream()
                        .map(limitsMapper::toDto)
                        .toList());
    }

    @NonNull
    public Collection<LimitDto> getLimitsByAccountIds(@NonNull final List<UUID> accountIds) {
        return circuitBreakerExecutor.run(
                        "getLimitsByAccountIds",
                        () -> api.getLimitsByAccountIds(accountIds).getLimits(),
                        () -> limitsRepository.findAllByAccountIdIn(accountIds).stream()
                                .map(limitsMapper::toDto)
                                .collect(Collectors.groupingBy(dc -> dc.getAccountId().toString()))
                ).values()
                .stream()
                .flatMap(List::stream)
                .toList();
    }
}

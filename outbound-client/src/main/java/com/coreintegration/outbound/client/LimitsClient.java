package com.coreintegration.outbound.client;

import com.coreintegration.circuitbreaker.CircuitBreakerExecutor;
import com.coreintegration.commons.model.LimitDto;
import com.coreintegration.fallbackengine.service.LimitsFallbackService;
import com.coreintegration.outbound.client.api.LimitsApi;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LimitsClient {

    private final LimitsApi api = new LimitsApi();
    private final CircuitBreakerExecutor circuitBreakerExecutor;
    private final LimitsFallbackService fallbackService;

    @NonNull
    public List<LimitDto> getLimitsByAccountId(@NotBlank final UUID accountId) {
        return circuitBreakerExecutor.run("getLimitsByAccountIds",
                () -> api.getLimitsByAccountId(accountId).getLimits(),
                () -> fallbackService.getFallbackByAccountId(accountId));
    }

    @NonNull
    public Collection<LimitDto> getLimitsByAccountIds(@NonNull final List<UUID> accountIds) {
        return circuitBreakerExecutor.run(
                        "getLimitsByAccountIds",
                        () -> api.getLimitsByAccountIds(accountIds).getLimits(),
                        () -> fallbackService.getFallbackByAccountIds(accountIds))
                .values()
                .stream()
                .flatMap(List::stream)
                .toList();
    }
}

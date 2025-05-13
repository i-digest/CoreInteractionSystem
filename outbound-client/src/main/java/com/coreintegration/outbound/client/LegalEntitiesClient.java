package com.coreintegration.outbound.client;

import com.coreintegration.circuitbreaker.CircuitBreakerExecutor;
import com.coreintegration.commons.model.LegalEntityDto;
import com.coreintegration.fallbackengine.service.LegalEntitiesFallbackService;
import com.coreintegration.outbound.client.api.LegalEntitiesApi;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LegalEntitiesClient {

    private final LegalEntitiesApi api = new LegalEntitiesApi();
    private final CircuitBreakerExecutor circuitBreakerExecutor;
    private final LegalEntitiesFallbackService fallbackService;


    @NonNull
    public List<LegalEntityDto> getLegalEntitiesByAccountId(@NotBlank final UUID accountId) {
        return circuitBreakerExecutor.run("getLegalEntitiesByAccountIds",
                () -> api.getLegalEntitiesByAccountId(accountId).getLegalEntities(),
                () -> fallbackService.getFallbackByAccountId(accountId));
    }

    @NonNull
    public Collection<LegalEntityDto> getLegalEntitiesByAccountIds(@NonNull final List<UUID> accountIds) {
        return circuitBreakerExecutor.run(
                        "getLegalEntitiesByAccountIds",
                        () -> api.getLegalEntitiesByAccountIds(accountIds).getLegalEntities(),
                        () -> fallbackService.getFallbackByAccountIds(accountIds))
                .values()
                .stream()
                .flatMap(List::stream)
                .toList();
    }
}

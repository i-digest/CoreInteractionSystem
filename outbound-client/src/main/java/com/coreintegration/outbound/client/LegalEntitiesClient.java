package com.coreintegration.outbound.client;

import com.coreintegration.circuitbreaker.CircuitBreakerExecutor;
import com.coreintegration.commons.model.LegalEntityDto;
import com.coreintegration.database.mapper.LegalEntitiesMapper;
import com.coreintegration.database.repository.LegalEntitiesRepository;
import com.coreintegration.outbound.client.api.LegalEntitiesApi;
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
public class LegalEntitiesClient {

    private final LegalEntitiesApi api = new LegalEntitiesApi();
    private final CircuitBreakerExecutor circuitBreakerExecutor;
    private final LegalEntitiesRepository legalEntitiesRepository;
    private final LegalEntitiesMapper legalEntitiesMapper;

    @NonNull
    public List<LegalEntityDto> getLegalEntitiesByAccountId(@NotBlank final UUID accountId) {
        return circuitBreakerExecutor.run("getLegalEntitiesByAccountIds",
                () -> api.getLegalEntitiesByAccountId(accountId).getLegalEntities(),
                () -> legalEntitiesRepository.findAllById(Collections.singleton(accountId)).stream()
                        .map(legalEntitiesMapper::toDto)
                        .toList());
    }

    @NonNull
    public Collection<LegalEntityDto> getLegalEntitiesByAccountIds(@NonNull final List<UUID> accountIds) {
        return circuitBreakerExecutor.run(
                        "getLegalEntitiesByAccountIds",
                        () -> api.getLegalEntitiesByAccountIds(accountIds).getLegalEntities(),
                        () -> legalEntitiesRepository.findAllByAccountIdIn(accountIds).stream()
                                .map(legalEntitiesMapper::toDto)
                                .collect(Collectors.groupingBy(dc -> dc.getAccountId().toString()))
                ).values()
                .stream()
                .flatMap(List::stream)
                .toList();
    }
}

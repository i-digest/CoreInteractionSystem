package com.coreintegration.outbound.client;

import com.coreintegration.commons.model.LegalEntityDto;
import com.coreintegration.outbound.client.api.LegalEntitiesApi;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class LegalEntitiesClient {

    private final LegalEntitiesApi api = new LegalEntitiesApi();

    @NonNull
    public List<LegalEntityDto> getLegalEntitiesByAccountId(@NotBlank final UUID accountId) {
        return api.getLegalEntitiesByAccountId(accountId)
                .getLegalEntities();
    }

    @NonNull
    public Collection<LegalEntityDto> getLegalEntitiesByAccountIds(@NonNull final List<UUID> accountIds) {
        return api.getLegalEntitiesByAccountIds(accountIds)
                .getLegalEntities()
                .values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}

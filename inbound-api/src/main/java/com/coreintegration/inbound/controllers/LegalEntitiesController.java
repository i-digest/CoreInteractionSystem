package com.coreintegration.inbound.controllers;

import com.coreintegration.commons.model.LegalEntitiesListResponseDto;
import com.coreintegration.commons.model.LegalEntitiesResponseDto;
import com.coreintegration.deduplication.annotation.Idempotent;
import com.coreintegration.inbound.api.LegalEntitiesApi;
import com.coreintegration.inbound.service.LegalEntitiesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LegalEntitiesController implements LegalEntitiesApi {

    private final LegalEntitiesService legalEntitiesService;

    @Override
    @Idempotent
    public ResponseEntity<LegalEntitiesResponseDto> getLegalEntitiesByAccountId(final UUID accountId) {
        return ResponseEntity.ok(legalEntitiesService.getLegalEntitiesByAccountId(accountId));
    }

    @Override
    @Idempotent
    public ResponseEntity<LegalEntitiesListResponseDto> getLegalEntitiesByAccountIds(@NotNull @Valid final List<UUID> accountIds) {
        return ResponseEntity.ok(legalEntitiesService.getLegalEntitiesByAccountIds(accountIds));
    }

}

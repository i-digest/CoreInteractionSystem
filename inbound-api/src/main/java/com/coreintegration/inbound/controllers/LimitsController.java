package com.coreintegration.inbound.controllers;

import com.coreintegration.commons.model.LimitsListResponseDto;
import com.coreintegration.commons.model.LimitsResponseDto;
import com.coreintegration.deduplication.annotation.Idempotent;
import com.coreintegration.inbound.api.LimitsApi;
import com.coreintegration.inbound.service.LimitsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LimitsController implements LimitsApi {

    private final LimitsService limitsService;

    @Override
    @Idempotent
    public ResponseEntity<LimitsResponseDto> getLimitsByAccountId(final UUID accountId) {
        return ResponseEntity.ok(limitsService.getLimitsByAccountId(accountId));
    }

    @Override
    @Idempotent
    public ResponseEntity<LimitsListResponseDto> getLimitsByAccountIds(@NotNull @Valid final List<UUID> accountIds) {
        return ResponseEntity.ok(limitsService.getLimitsByAccountIds(accountIds));
    }

}

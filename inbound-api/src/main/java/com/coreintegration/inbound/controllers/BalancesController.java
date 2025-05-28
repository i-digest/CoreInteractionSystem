package com.coreintegration.inbound.controllers;

import com.coreintegration.commons.model.BalanceListResponseDto;
import com.coreintegration.commons.model.BalanceResponseDto;
import com.coreintegration.deduplication.annotation.Idempotent;
import com.coreintegration.inbound.api.BalancesApi;
import com.coreintegration.inbound.service.BalancesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BalancesController implements BalancesApi {

    private final BalancesService service;

    @Override
    @Idempotent
    public ResponseEntity<BalanceResponseDto> getBalanceByAccountId(final UUID accountId) {
        return ResponseEntity.ok(service.getBalanceById(accountId));
    }

    @Override
    @Idempotent
    public ResponseEntity<BalanceListResponseDto> getBalancesByAccountIds(@NotNull @Valid final List<UUID> accountIds) {
        return ResponseEntity.ok(service.getBalancesByIds(accountIds));
    }

}

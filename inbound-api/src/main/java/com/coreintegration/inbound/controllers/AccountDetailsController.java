package com.coreintegration.inbound.controllers;

import com.coreintegration.commons.model.AccountDetailsListResponseDto;
import com.coreintegration.commons.model.AccountDetailsResponseDto;
import com.coreintegration.deduplication.annotation.Idempotent;
import com.coreintegration.inbound.api.AccountDetailsApi;
import com.coreintegration.inbound.service.AccountDetailsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccountDetailsController implements AccountDetailsApi {

    private final AccountDetailsService service;

    @Override
    @Idempotent
    public ResponseEntity<AccountDetailsResponseDto> getAccountDetailsById(final UUID accountId) {
        return ResponseEntity.ok(service.getAccountDetailsById(accountId));
    }

    @Override
    @Idempotent
    public ResponseEntity<AccountDetailsListResponseDto> getAccountsDetailsByIds(@NotNull @Valid final List<UUID> accountIds) {
        return ResponseEntity.ok(service.getAccountsDetailsByIds(accountIds));
    }

}

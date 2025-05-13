package com.coreintegration.inbound.controllers;

import com.coreintegration.commons.model.LoansListResponseDto;
import com.coreintegration.commons.model.LoansResponseDto;
import com.coreintegration.deduplication.annotation.Idempotent;
import com.coreintegration.inbound.api.LoansApi;
import com.coreintegration.inbound.service.LoansService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LoansController implements LoansApi {

    private final LoansService loansService;

    @Override
    @Idempotent
    public ResponseEntity<LoansResponseDto> getLoansByAccountId(@NotBlank @Valid final UUID accountId) {
        return ResponseEntity.ok(loansService.getLoansByAccountId(accountId));
    }

    @Override
    @Idempotent
    public ResponseEntity<LoansListResponseDto> getLoansByAccountIds(@Size(min = 1, max = 1000) @Valid final List<UUID> accountIds) {
        return ResponseEntity.ok(loansService.getLoansByAccountIds(accountIds));
    }

}

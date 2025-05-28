package com.coreintegration.inbound.controllers;

import com.coreintegration.commons.model.DebitCardListResponseDto;
import com.coreintegration.commons.model.DebitCardResponseDto;
import com.coreintegration.deduplication.annotation.Idempotent;
import com.coreintegration.inbound.api.DebitCardsApi;
import com.coreintegration.inbound.service.DebitCardsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DebitCardsController implements DebitCardsApi {

    private final DebitCardsService debitCardsService;

    @Override
    @Idempotent
    public ResponseEntity<DebitCardResponseDto> getDebitCardsByAccountId(final UUID accountId) {
        return ResponseEntity.ok(debitCardsService.getDebitCardsByAccountId(accountId));
    }

    @Override
    @Idempotent
    public ResponseEntity<DebitCardListResponseDto> getDebitCardsByAccountIds(@NotNull @Valid final List<UUID> accountIds) {
        return ResponseEntity.ok(debitCardsService.getDebitCardsByAccountIds(accountIds));
    }

}

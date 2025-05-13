package com.coreintegration.inbound.api;

import com.coreintegration.commons.model.DebitCardListResponseDto;
import com.coreintegration.commons.model.DebitCardResponseDto;
import com.coreintegration.inbound.service.DebitCardsService;
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
public class DebitCardsController implements DebitCardsApi {

    private final DebitCardsService debitCardsService;

    @Override
    public ResponseEntity<DebitCardResponseDto> getDebitCardsByAccountId(@NotBlank @Valid final UUID accountId) {
        return ResponseEntity.ok(debitCardsService.getDebitCardsByAccountId(accountId));
    }

    @Override
    public ResponseEntity<DebitCardListResponseDto> getDebitCardsByAccountIds(@Size(min = 1, max = 1000) @Valid final List<UUID> accountIds) {
        return ResponseEntity.ok(debitCardsService.getDebitCardsByAccountIds(accountIds));
    }

}

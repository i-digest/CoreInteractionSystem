package com.coreintegration.inbound.api;

import com.coreintegration.commons.model.DebitCardListResponse;
import com.coreintegration.commons.model.DebitCardResponse;
import com.coreintegration.inbound.service.DebitCardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DebitCardsController implements DebitCardsApi {

    private final DebitCardsService debitCardsService;

    @Override
    public ResponseEntity<DebitCardResponse> getDebitCardsByAccountId(String accountId) {
        return ResponseEntity.ok(debitCardsService.getDebitCardByAccountId(accountId));
    }

    @Override
    public ResponseEntity<DebitCardListResponse> getDebitCardsByAccountIds(List<String> accountIds) {
        return ResponseEntity.ok(debitCardsService.getDebitCardsByAccountIds(accountIds));
    }

}

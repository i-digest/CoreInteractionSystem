package com.coreintegration.inbound.api;

import com.coreintegration.commons.model.LoansListResponse;
import com.coreintegration.commons.model.LoansResponse;
import com.coreintegration.inbound.service.LoansService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoansController implements LoansApi {

    private final LoansService loansService;

    @Override
    public ResponseEntity<LoansResponse> getLimitsByAccountId(String accountId) {
        return ResponseEntity.ok(loansService.getLoansByAccountId(accountId));
    }

    @Override
    public ResponseEntity<LoansListResponse> getLimitsByAccountIds(List<String> accountIds) {
        return ResponseEntity.ok(loansService.getLoansByAccountIds(accountIds));
    }

}

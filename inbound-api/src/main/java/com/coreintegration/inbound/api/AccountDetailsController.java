package com.coreintegration.inbound.api;

import com.coreintegration.commons.model.AccountDetailsListResponse;
import com.coreintegration.commons.model.AccountDetailsResponse;
import com.coreintegration.inbound.service.AccountDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountDetailsController implements AccountDetailsApi {

    private final AccountDetailsService service;

    @Override
    public ResponseEntity<AccountDetailsResponse> getAccountDetailsById(final String accountId) {
        return ResponseEntity.ok(service.getAccountDetailsById(accountId));
    }

    @Override
    public ResponseEntity<AccountDetailsListResponse> getAccountsDetailsByIds(final List<String> accountIds) {
        return ResponseEntity.ok(service.getAccountsDetailsByIds(accountIds));
    }
}

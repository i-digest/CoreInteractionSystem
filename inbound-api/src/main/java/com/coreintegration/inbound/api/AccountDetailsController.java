package com.coreintegration.inbound.api;

import com.coreintegration.commons.model.AccountDetailsListResponse;
import com.coreintegration.commons.model.AccountDetailsResponse;
import com.coreintegration.inbound.service.AccountDetailsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountDetailsController implements AccountDetailsApi {

    private final AccountDetailsService service;

    @Override
    public ResponseEntity<AccountDetailsResponse> getAccountDetailsById(@NotBlank @Valid final String accountId) {
        return ResponseEntity.ok(service.getAccountDetailsById(accountId));
    }

    @Override
    public ResponseEntity<AccountDetailsListResponse> getAccountsDetailsByIds(@Size(min = 1, max = 1000) @Valid final List<String> accountIds) {
        return ResponseEntity.ok(service.getAccountsDetailsByIds(accountIds));
    }

}

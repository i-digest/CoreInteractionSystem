package com.coreintegration.inbound.api;

import com.coreintegration.commons.model.AccountDetailsListResponseDto;
import com.coreintegration.commons.model.AccountDetailsResponseDto;
import com.coreintegration.inbound.service.AccountDetailsService;
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
public class AccountDetailsController implements AccountDetailsApi {

    private final AccountDetailsService service;

    @Override
    public ResponseEntity<AccountDetailsResponseDto> getAccountDetailsById(@NotBlank @Valid final UUID accountId) {
        return ResponseEntity.ok(service.getAccountDetailsById(accountId));
    }

    @Override
    public ResponseEntity<AccountDetailsListResponseDto> getAccountsDetailsByIds(@Size(min = 1, max = 1000) @Valid final List<UUID> accountIds) {
        return ResponseEntity.ok(service.getAccountsDetailsByIds(accountIds));
    }

}

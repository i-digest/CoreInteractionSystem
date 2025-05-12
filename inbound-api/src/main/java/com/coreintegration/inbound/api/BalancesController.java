package com.coreintegration.inbound.api;

import com.coreintegration.commons.model.BalanceListResponse;
import com.coreintegration.commons.model.BalanceResponse;
import com.coreintegration.inbound.service.BalancesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BalancesController implements BalancesApi {

    private final BalancesService service;

    @Override
    public ResponseEntity<BalanceResponse> getBalanceByAccountId(@NotBlank @Valid final String accountId) {
        return ResponseEntity.ok(service.getBalanceById(accountId));
    }

    @Override
    public ResponseEntity<BalanceListResponse> getBalancesByAccountIds(@Size(min = 1, max = 1000) @Valid final List<String> accountIds) {
        return ResponseEntity.ok(service.getBalancesByIds(accountIds));
    }

}

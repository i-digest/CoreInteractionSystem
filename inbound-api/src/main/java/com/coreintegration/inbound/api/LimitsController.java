package com.coreintegration.inbound.api;

import com.coreintegration.commons.model.LimitsListResponse;
import com.coreintegration.commons.model.LimitsResponse;
import com.coreintegration.inbound.service.LimitsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LimitsController implements LimitsApi {

    private final LimitsService limitsService;

    @Override
    public ResponseEntity<LimitsResponse> getLimitsByAccountId(String accountId) {
        return ResponseEntity.ok(limitsService.getLimitsByAccountId(accountId));
    }

    @Override
    public ResponseEntity<LimitsListResponse> getLimitsByAccountIds(List<String> accountIds) {
        return ResponseEntity.ok(limitsService.getLimitsByAccountIds(accountIds));
    }

}

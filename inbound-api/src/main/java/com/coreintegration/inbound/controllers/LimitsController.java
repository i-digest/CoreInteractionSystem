package com.coreintegration.inbound.controllers;

import com.coreintegration.commons.model.LimitsListResponseDto;
import com.coreintegration.commons.model.LimitsResponseDto;
import com.coreintegration.inbound.api.LimitsApi;
import com.coreintegration.inbound.service.LimitsService;
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
public class LimitsController implements LimitsApi {

    private final LimitsService limitsService;

    @Override
    public ResponseEntity<LimitsResponseDto> getLimitsByAccountId(@NotBlank @Valid final UUID accountId) {
        return ResponseEntity.ok(limitsService.getLimitsByAccountId(accountId));
    }

    @Override
    public ResponseEntity<LimitsListResponseDto> getLimitsByAccountIds(@Size(min = 1, max = 1000) @Valid final List<UUID> accountIds) {
        return ResponseEntity.ok(limitsService.getLimitsByAccountIds(accountIds));
    }

}

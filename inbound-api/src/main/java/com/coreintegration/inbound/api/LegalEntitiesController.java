package com.coreintegration.inbound.api;

import com.coreintegration.commons.model.LegalEntitiesListResponse;
import com.coreintegration.commons.model.LegalEntitiesResponse;
import com.coreintegration.inbound.service.LegalEntitiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LegalEntitiesController implements LegalEntitiesApi {

    private final LegalEntitiesService legalEntitiesService;

    @Override
    public ResponseEntity<LegalEntitiesResponse> getLegalEntitiesByAccountId(String accountId) {
        return ResponseEntity.ok(legalEntitiesService.getLegalEntitiesByAccountId(accountId));
    }

    @Override
    public ResponseEntity<LegalEntitiesListResponse> getLegalEntitiesByAccountIds(List<String> accountIds) {
        return ResponseEntity.ok(legalEntitiesService.getLegalEntitiesByAccountIds(accountIds));
    }

}

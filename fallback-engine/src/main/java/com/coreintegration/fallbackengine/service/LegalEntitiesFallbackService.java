package com.coreintegration.fallbackengine.service;

import com.coreintegration.commons.model.LegalEntityDto;
import com.coreintegration.database.mapper.LegalEntitiesMapper;
import com.coreintegration.database.repository.LegalEntitiesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LegalEntitiesFallbackService {

    private final LegalEntitiesRepository repository;
    private final LegalEntitiesMapper legalEntitiesMapper;

    public List<LegalEntityDto> getFallbackByAccountId(@NonNull final UUID accountId) {
        return repository.findAllById(Collections.singleton(accountId)).stream()
                .map(legalEntitiesMapper::toDto)
                .toList();
    }

    public Map<String, List<LegalEntityDto>> getFallbackByAccountIds(@NonNull final List<UUID> accountIds) {
        return repository.findAllByAccountIdIn(accountIds).stream()
                .map(legalEntitiesMapper::toDto)
                .collect(Collectors.groupingBy(dc -> dc.getAccountId().toString()));
    }
}

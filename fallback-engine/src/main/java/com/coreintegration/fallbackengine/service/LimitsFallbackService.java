package com.coreintegration.fallbackengine.service;

import com.coreintegration.commons.model.LimitDto;
import com.coreintegration.database.mapper.LimitsMapper;
import com.coreintegration.database.repository.LimitsRepository;
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
public class LimitsFallbackService {

    private final LimitsRepository repository;
    private final LimitsMapper limitsMapper;

    public List<LimitDto> getFallbackByAccountId(@NonNull final UUID accountId) {
        return repository.findAllById(Collections.singleton(accountId)).stream()
                .map(limitsMapper::toDto)
                .toList();
    }

    public Map<String, List<LimitDto>> getFallbackByAccountIds(@NonNull final List<UUID> accountIds) {
        return repository.findAllByAccountIdIn(accountIds).stream()
                .map(limitsMapper::toDto)
                .collect(Collectors.groupingBy(dc -> dc.getAccountId().toString()));
    }
}

package com.coreintegration.fallbackengine.service;

import com.coreintegration.commons.model.BalanceDto;
import com.coreintegration.database.mapper.BalanceMapper;
import com.coreintegration.database.repository.BalanceRepository;
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
public class BalancesFallbackService {

    private final BalanceRepository repository;
    private final BalanceMapper balanceMapper;

    public List<BalanceDto> getFallbackByAccountId(@NonNull final UUID accountId) {
        return repository.findAllById(Collections.singleton(accountId)).stream()
                .map(balanceMapper::toDto)
                .toList();
    }

    public Map<String, List<BalanceDto>> getFallbackByAccountIds(@NonNull final List<UUID> accountIds) {
        return repository.findAllByAccountIdIn(accountIds).stream()
                .map(balanceMapper::toDto)
                .collect(Collectors.groupingBy(dc -> dc.getAccountId().toString()));
    }

}

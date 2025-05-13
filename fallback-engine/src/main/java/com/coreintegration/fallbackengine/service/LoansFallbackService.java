package com.coreintegration.fallbackengine.service;

import com.coreintegration.commons.model.LoanDto;
import com.coreintegration.database.mapper.LoansMapper;
import com.coreintegration.database.repository.LoansRepository;
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
public class LoansFallbackService {

    private final LoansRepository repository;
    private final LoansMapper loansMapper;

    public List<LoanDto> getFallbackByAccountId(@NonNull final UUID accountId) {
        return repository.findAllById(Collections.singleton(accountId)).stream()
                .map(loansMapper::toDto)
                .toList();
    }

    public Map<String, List<LoanDto>> getFallbackByAccountIds(@NonNull final List<UUID> accountIds) {
        return repository.findAllByAccountIdIn(accountIds).stream()
                .map(loansMapper::toDto)
                .collect(Collectors.groupingBy(dc -> dc.getAccountId().toString()));
    }
}

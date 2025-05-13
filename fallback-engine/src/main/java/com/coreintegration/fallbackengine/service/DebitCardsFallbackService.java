package com.coreintegration.fallbackengine.service;

import com.coreintegration.commons.model.DebitCardDto;
import com.coreintegration.database.mapper.DebitCardsMapper;
import com.coreintegration.database.repository.DebitCardsRepository;
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
public class DebitCardsFallbackService {

    private final DebitCardsRepository repository;
    private final DebitCardsMapper debitCardsMapper;

    public List<DebitCardDto> getFallbackByAccountId(@NonNull final UUID accountId) {
        return repository.findAllById(Collections.singleton(accountId)).stream()
                .map(debitCardsMapper::toDto)
                .toList();
    }

    public Map<String, List<DebitCardDto>> getFallbackByAccountIds(@NonNull final List<UUID> accountIds) {
        return repository.findAllByAccountIdIn(accountIds)
                .stream()
                .map(debitCardsMapper::toDto)
                .collect(Collectors.groupingBy(dc -> dc.getAccountId().toString()));
    }

}

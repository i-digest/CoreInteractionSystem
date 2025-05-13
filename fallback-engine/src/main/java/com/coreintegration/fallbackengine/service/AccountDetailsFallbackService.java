package com.coreintegration.fallbackengine.service;

import com.coreintegration.commons.model.AccountDetailsDto;
import com.coreintegration.database.mapper.AccountDetailsMapper;
import com.coreintegration.database.repository.AccountDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountDetailsFallbackService {

    private final AccountDetailsRepository repository;
    private final AccountDetailsMapper accountDetailsMapper;

    public AccountDetailsDto getFallbackByAccountId(@NonNull final UUID accountId) {
        return repository.findById(accountId)
                .map(accountDetailsMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("Account details not found"));
    }

    public Map<String, AccountDetailsDto> getFallbackByAccountIds(@NonNull final List<UUID> accountIds) {
        return repository.findAllByIdIn(accountIds).stream()
                .map(accountDetailsMapper::toDto)
                .collect(Collectors.toMap(accountDetailsDto -> accountDetailsDto.getId().toString(), accountDetailsDto -> accountDetailsDto));
    }

}

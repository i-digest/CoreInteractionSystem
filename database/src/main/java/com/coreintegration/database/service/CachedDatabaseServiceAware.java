package com.coreintegration.database.service;

import com.coreintegration.commons.model.AccountDetails;
import com.coreintegration.database.entity.AccountDetailsEntity;
import com.coreintegration.database.mapper.AccountDetailsMapper;
import com.coreintegration.database.repository.AccountDetailsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class CachedDatabaseServiceAware {

    private final CacheManager cacheManager;
    private final AccountDetailsRepository accountDetailsRepository;
    private final AccountDetailsMapper accountDetailsMapper;

    @Cacheable(value = "accountDetailsCache", key = "#accountId", unless = "#result == null")
    public AccountDetails getAccountDetails(@NonNull final String accountId, Supplier<AccountDetails> supplier) {
        final AccountDetailsEntity accountDetails = accountDetailsRepository.findById(accountId)
                .orElseGet(() -> accountDetailsMapper.toEntity(supplier.get()));

        if (accountDetails.getLastUpdateDate().isBefore(LocalDateTime.now().minusMinutes(10))) {
            //todo: call core
            //todo: evict update event
        }
        return accountDetailsMapper.toDto(accountDetails);
    }
}

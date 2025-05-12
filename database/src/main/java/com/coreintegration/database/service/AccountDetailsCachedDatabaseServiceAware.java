package com.coreintegration.database.service;

import com.coreintegration.commons.model.AccountDetails;
import com.coreintegration.database.entity.AccountDetailsEntity;
import com.coreintegration.database.mapper.AccountDetailsMapper;
import com.coreintegration.database.repository.AccountDetailsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AccountDetailsCachedDatabaseServiceAware {

    private static final String CACHE_NAME = "accountDetailsCache";
    private final CacheManager cacheManager;
    private final AccountDetailsRepository accountDetailsRepository;
    private final AccountDetailsMapper accountDetailsMapper;

    @Cacheable(value = CACHE_NAME, key = "#accountId", unless = "#result == null")
    public AccountDetails getAccountDetails(@NonNull final String accountId, @NonNull final Supplier<AccountDetails> supplier) {
        return getDetailsAndUpdateFromSupplier(accountId, supplier);
    }

    @NonNull
    public List<AccountDetails> getListOfAccountDetails(@NonNull final List<String> accountIds, @NonNull final List<String> idsToLoadFromCore, @NonNull final Supplier<List<AccountDetails>> supplier) {
        final List<AccountDetails> details = new ArrayList<>(accountIds.size());

        final List<String> idMissedFromCache = new ArrayList<>();
        idsToLoadFromCore.clear();
        final Cache accountDetailsCache = cacheManager.getCache(CACHE_NAME);
        accountIds.forEach(accountId -> {
            if (accountDetailsCache != null) {
                final AccountDetails accountDetails = accountDetailsCache.get(accountId, AccountDetails.class);
                if (accountDetails != null) {
                    details.add(accountDetails);
                } else {
                    idMissedFromCache.add(accountId);
                }
            } else {
                idMissedFromCache.add(accountId);
            }
        });

        if (!idMissedFromCache.isEmpty()) {
            details.addAll(getDetailsListAndUpdateFromSupplier(idMissedFromCache, idsToLoadFromCore, supplier));
            if (accountDetailsCache != null) {
                details.forEach(accountDetails -> accountDetailsCache.put(accountDetails.getId(), accountDetails));
            }
        }

        return details;
    }

    private List<AccountDetails> getDetailsListAndUpdateFromSupplier(@NonNull final List<String> accountIds, List<String> idsToLoad, @NonNull final Supplier<List<AccountDetails>> supplier) {
        final List<AccountDetailsEntity> allByIdIn = accountDetailsRepository.findAllByIdIn(accountIds);
        final Map<String, AccountDetailsEntity> map = allByIdIn.stream().collect(Collectors.toMap(AccountDetailsEntity::getId, Function.identity()));

        accountIds.stream()
                .filter(id -> !map.containsKey(id))
                .forEach(idsToLoad::add);
        final List<AccountDetails> detailsFromCore = supplier.get();
        if (!detailsFromCore .isEmpty()) {
            accountDetailsRepository.saveAllAsync(accountDetailsMapper.toEntity(detailsFromCore));
        }
        detailsFromCore.addAll(accountDetailsMapper.toDto(allByIdIn));

        return detailsFromCore;
    }

    private AccountDetails getDetailsAndUpdateFromSupplier(final String accountId, final Supplier<AccountDetails> supplier) {
        final AccountDetailsEntity entity = loadOrGetFromSupplier(accountId, supplier);
        return accountDetailsMapper.toDto(entity);
    }

    private AccountDetailsEntity loadOrGetFromSupplier(final String accountId, final Supplier<AccountDetails> supplier) {
        return accountDetailsRepository.findById(accountId)
                .orElseGet(() -> accountDetailsMapper.toEntity(supplier.get()));
    }
}

package com.coreintegration.database.service;

import com.coreintegration.commons.model.AccountDetailsDto;
import com.coreintegration.database.entity.AccountDetails;
import com.coreintegration.database.mapper.AccountDetailsMapper;
import com.coreintegration.database.repository.AccountDetailsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
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
    public AccountDetailsDto getAccountDetails(@NonNull final UUID accountId, @NonNull final Supplier<AccountDetailsDto> supplier) {
        return getDetailsAndUpdateFromSupplier(accountId, supplier);
    }

    @NonNull
    public List<AccountDetailsDto> getListOfAccountDetails(@NonNull final List<UUID> accountIds, @NonNull final List<UUID> idsToLoadFromCore, @NonNull final Supplier<Collection<AccountDetailsDto>> supplier) {
        final List<AccountDetailsDto> details = new ArrayList<>(accountIds.size());

        final List<UUID> idMissedFromCache = new ArrayList<>();
        idsToLoadFromCore.clear();
        final Cache accountDetailsCache = cacheManager.getCache(CACHE_NAME);
        accountIds.forEach(accountId -> {
            if (accountDetailsCache != null) {
                final AccountDetailsDto accountDetails = accountDetailsCache.get(accountId, AccountDetailsDto.class);
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

    private Collection<AccountDetailsDto> getDetailsListAndUpdateFromSupplier(@NonNull final List<UUID> accountIds, List<UUID> idsToLoad, @NonNull final Supplier<Collection<AccountDetailsDto>> supplier) {
        final List<AccountDetails> allByIdIn = accountDetailsRepository.findAllByIdIn(accountIds);
        final Map<UUID, AccountDetails> map = allByIdIn.stream().collect(Collectors.toMap(AccountDetails::getId, Function.identity()));

        accountIds.stream()
                .filter(id -> !map.containsKey(id))
                .forEach(idsToLoad::add);
        final Collection<AccountDetailsDto> detailsFromCore = supplier.get();
        if (!detailsFromCore .isEmpty()) {
            accountDetailsRepository.saveAllAsync(accountDetailsMapper.toEntityList(detailsFromCore));
        }
        detailsFromCore.addAll(accountDetailsMapper.toDtoList(allByIdIn));

        return detailsFromCore;
    }

    private AccountDetailsDto getDetailsAndUpdateFromSupplier(final UUID accountId, final Supplier<AccountDetailsDto> supplier) {
        final AccountDetails entity = loadOrGetFromSupplier(accountId, supplier);
        return accountDetailsMapper.toDto(entity);
    }

    private AccountDetails loadOrGetFromSupplier(final UUID accountId, final Supplier<AccountDetailsDto> supplier) {
        return accountDetailsRepository.findById(accountId)
                .orElseGet(() -> accountDetailsMapper.toEntity(supplier.get()));
    }
}

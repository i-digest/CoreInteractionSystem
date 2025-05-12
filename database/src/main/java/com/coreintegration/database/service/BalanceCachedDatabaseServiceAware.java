package com.coreintegration.database.service;

import com.coreintegration.commons.model.Balance;
import com.coreintegration.database.entity.BalanceEntity;
import com.coreintegration.database.mapper.BalanceMapper;
import com.coreintegration.database.repository.BalanceRepository;
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
public class BalanceCachedDatabaseServiceAware {

    private static final String CACHE_NAME = "balanceCache";
    private final CacheManager cacheManager;
    private final BalanceRepository balanceRepository;
    private final BalanceMapper balanceMapper;

    @Cacheable(value = CACHE_NAME, key = "#balanceId", unless = "#result == null")
    public Balance getBalance(@NonNull final String balanceId, @NonNull final Supplier<Balance> supplier) {
        return getDetailsAndUpdateFromSupplier(balanceId, supplier);
    }

    @NonNull
    public List<Balance> getListOfBalance(@NonNull final List<String> balanceIds, @NonNull final List<String> idsToLoadFromCore, @NonNull final Supplier<List<Balance>> supplier) {
        final List<Balance> balances = new ArrayList<>(balanceIds.size());

        final List<String> idMissedFromCache = new ArrayList<>();
        idsToLoadFromCore.clear();
        final Cache balancesCache = cacheManager.getCache(CACHE_NAME);
        balanceIds.forEach(balanceId -> {
            if (balancesCache != null) {
                final Balance balance = balancesCache.get(balanceId, Balance.class);
                if (balance != null) {
                    balances.add(balance);
                } else {
                    idMissedFromCache.add(balanceId);
                }
            } else {
                idMissedFromCache.add(balanceId);
            }
        });

        if (!idMissedFromCache.isEmpty()) {
            balances.addAll(getDetailsListAndUpdateFromSupplier(idMissedFromCache, idsToLoadFromCore, supplier));
            if (balancesCache != null) {
                balances.forEach(balance -> balancesCache.put(balance.getAccountId(), balance));
            }
        }

        return balances;
    }

    private List<Balance> getDetailsListAndUpdateFromSupplier(@NonNull final List<String> balanceIds, List<String> idsToLoad, @NonNull final Supplier<List<Balance>> supplier) {
        final List<BalanceEntity> allByIdIn = balanceRepository.findAllByAccountIdIn(balanceIds);
        final Map<String, BalanceEntity> map = allByIdIn.stream().collect(Collectors.toMap(BalanceEntity::getAccountId, Function.identity()));

        balanceIds.stream()
                .filter(id -> !map.containsKey(id))
                .forEach(idsToLoad::add);
        final List<Balance> detailsFromCore = supplier.get();
        if (!detailsFromCore .isEmpty()) {
            balanceRepository.saveAllAsync(balanceMapper.toEntity(detailsFromCore));
        }
        detailsFromCore.addAll(balanceMapper.toDto(allByIdIn));

        return detailsFromCore;
    }

    private Balance getDetailsAndUpdateFromSupplier(final String balanceId, final Supplier<Balance> supplier) {
        final BalanceEntity entity = loadOrGetFromSupplier(balanceId, supplier);
        return balanceMapper.toDto(entity);
    }

    private BalanceEntity loadOrGetFromSupplier(final String balanceId, final Supplier<Balance> supplier) {
        return balanceRepository.findById(balanceId)
                .orElseGet(() -> balanceMapper.toEntity(supplier.get()));
    }
}

package com.coreintegration.database.service;

import com.coreintegration.commons.model.BalanceDto;
import com.coreintegration.database.entity.Balance;
import com.coreintegration.database.mapper.BalanceMapper;
import com.coreintegration.database.repository.BalanceRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BalancesCachedDatabaseServiceAware {

    private static final String CACHE_NAME = "balancesCache";
    private final CacheManager cacheManager;
    private final BalanceRepository balanceRepository;
    private final BalanceMapper balanceMapper;

    @NonNull
    public List<BalanceDto> getBalancesByAccountId(@NonNull final UUID accountId, @NonNull final List<UUID> idsToLoadFromCore, @NonNull final Supplier<Collection<BalanceDto>> supplier) {
        return getBalancesByAccountIds(List.of(accountId), idsToLoadFromCore, supplier);
    }

    @NonNull
    public List<BalanceDto> getBalancesByAccountIds(@NonNull final List<UUID> accountIds, @NonNull final List<UUID> idsToLoadFromCore, @NonNull final Supplier<Collection<BalanceDto>> supplier) {
        final List<BalanceDto> balances = new ArrayList<>(accountIds.size());

        final List<UUID> idMissedFromCache = new ArrayList<>();
        idsToLoadFromCore.clear();
        final Cache balancesCache = cacheManager.getCache(CACHE_NAME);
        accountIds.forEach(accountId -> {
            if (balancesCache != null) {
                final BalanceDto balance = balancesCache.get(accountId, BalanceDto.class);
                if (balance != null) {
                    balances.add(balance);
                } else {
                    idMissedFromCache.add(accountId);
                }
            } else {
                idMissedFromCache.add(accountId);
            }
        });

        if (!idMissedFromCache.isEmpty()) {
            balances.addAll(getBalanceListAndUpdateFromSupplier(idMissedFromCache, idsToLoadFromCore, supplier));
            if (balancesCache != null) {
                balances.forEach(balance -> balancesCache.put(balance.getAccountId(), balance));
            }
        }

        return balances;
    }

    private Collection<BalanceDto> getBalanceListAndUpdateFromSupplier(final List<UUID> balanceIds, List<UUID> idsToLoad,final Supplier<Collection<BalanceDto>> supplier) {
        final List<Balance> allByIdIn = balanceRepository.findAllByAccountIdIn(balanceIds);
        final Map<UUID, Balance> map = allByIdIn.stream().collect(Collectors.toMap(Balance::getAccountId, Function.identity()));

        balanceIds.stream()
                .filter(id -> !map.containsKey(id))
                .forEach(idsToLoad::add);
        final Collection<BalanceDto> balancesFromCore = supplier.get();
        if (!balancesFromCore .isEmpty()) {
            balanceRepository.saveAllAsync(balanceMapper.toEntityList(balancesFromCore));
        }
        balancesFromCore.addAll(balanceMapper.toDtoList(allByIdIn));

        return balancesFromCore;
    }

}

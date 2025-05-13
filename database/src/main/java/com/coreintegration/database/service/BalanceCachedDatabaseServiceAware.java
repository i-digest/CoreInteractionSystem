package com.coreintegration.database.service;

import com.coreintegration.commons.model.BalanceDto;
import com.coreintegration.database.entity.Balance;
import com.coreintegration.database.mapper.BalanceMapper;
import com.coreintegration.database.repository.BalanceRepository;
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
public class BalanceCachedDatabaseServiceAware {

    private static final String CACHE_NAME = "balanceCache";
    private final CacheManager cacheManager;
    private final BalanceRepository balanceRepository;
    private final BalanceMapper balanceMapper;

    @Cacheable(value = CACHE_NAME, key = "#balanceId", unless = "#result == null")
    public BalanceDto getBalance(@NonNull final UUID balanceId, @NonNull final Supplier<BalanceDto> supplier) {
        return getBalancesAndUpdateFromSupplier(balanceId, supplier);
    }

    @NonNull
    public List<BalanceDto> getListOfBalance(@NonNull final List<UUID> balanceIds, @NonNull final List<UUID> idsToLoadFromCore, @NonNull final Supplier<Collection<BalanceDto>> supplier) {
        final List<BalanceDto> balances = new ArrayList<>(balanceIds.size());

        final List<UUID> idMissedFromCache = new ArrayList<>();
        idsToLoadFromCore.clear();
        final Cache balancesCache = cacheManager.getCache(CACHE_NAME);
        balanceIds.forEach(balanceId -> {
            if (balancesCache != null) {
                final BalanceDto balance = balancesCache.get(balanceId, BalanceDto.class);
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
        final Collection<BalanceDto> detailsFromCore = supplier.get();
        if (!detailsFromCore .isEmpty()) {
            balanceRepository.saveAllAsync(balanceMapper.toEntityList(detailsFromCore));
        }
        detailsFromCore.addAll(balanceMapper.toDtoList(allByIdIn));

        return detailsFromCore;
    }

    private BalanceDto getBalancesAndUpdateFromSupplier(final UUID balanceId, final Supplier<BalanceDto> supplier) {
        final Balance entity = loadOrGetFromSupplier(balanceId, supplier);
        return balanceMapper.toDto(entity);
    }

    private Balance loadOrGetFromSupplier(final UUID balanceId, final Supplier<BalanceDto> supplier) {
        return balanceRepository.findById(balanceId)
                .orElseGet(() -> balanceMapper.toEntity(supplier.get()));
    }
}

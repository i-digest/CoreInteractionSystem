package com.coreintegration.database.service;

import com.coreintegration.commons.model.LimitDto;
import com.coreintegration.database.entity.Limit;
import com.coreintegration.database.mapper.LimitsMapper;
import com.coreintegration.database.repository.LimitsRepository;
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
public class LimitsCachedDatabaseServiceAware {

    private static final String CACHE_NAME = "limitsCache";
    private final CacheManager cacheManager;
    private final LimitsRepository limitsRepository;
    private final LimitsMapper limitsMapper;

    @NonNull
    public List<LimitDto> getLimitsByAccountId(@NonNull final UUID accountId, @NonNull final List<UUID> idsToLoadFromCore, @NonNull final Supplier<Collection<LimitDto>> supplier) {
        return getLimitsByAccountIds(List.of(accountId), idsToLoadFromCore, supplier);
    }

    @NonNull
    public List<LimitDto> getLimitsByAccountIds(@NonNull final List<UUID> accountIds, @NonNull final List<UUID> idsToLoadFromCore, @NonNull final Supplier<Collection<LimitDto>> supplier) {
        final List<LimitDto> limits = new ArrayList<>(accountIds.size());

        final List<UUID> idMissedFromCache = new ArrayList<>();
        idsToLoadFromCore.clear();
        final Cache limitCache = cacheManager.getCache(CACHE_NAME);
        accountIds.forEach(accountId -> {
            if (limitCache != null) {
                final LimitDto limit = limitCache.get(accountId, LimitDto.class);
                if (limit != null) {
                    limits.add(limit);
                } else {
                    idMissedFromCache.add(accountId);
                }
            } else {
                idMissedFromCache.add(accountId);
            }
        });

        if (!idMissedFromCache.isEmpty()) {
            limits.addAll(getLimitsListAndUpdateFromSupplier(idMissedFromCache, idsToLoadFromCore, supplier));
            if (limitCache != null) {
                limits.forEach(balance -> limitCache.put(balance.getAccountId(), balance));
            }
        }

        return limits;
    }

    private Collection<LimitDto> getLimitsListAndUpdateFromSupplier(final List<UUID> accountIds, List<UUID> idsToLoad, final Supplier<Collection<LimitDto>> supplier) {
        final List<Limit> allByIdIn = limitsRepository.findAllByAccountIdIn(accountIds);
        final Map<UUID, Limit> map = allByIdIn.stream().collect(Collectors.toMap(Limit::getAccountId, Function.identity()));

        accountIds.stream()
                .filter(id -> !map.containsKey(id))
                .forEach(idsToLoad::add);
        final Collection<LimitDto> limitsFromCore = supplier.get();
        if (!limitsFromCore .isEmpty()) {
            limitsRepository.saveAllAsync(limitsMapper.toEntityList(limitsFromCore));
        }
        limitsFromCore.addAll(limitsMapper.toDtoList(allByIdIn));

        return limitsFromCore;
    }
}

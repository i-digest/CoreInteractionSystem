package com.coreintegration.database.service;

import com.coreintegration.commons.model.LegalEntityDto;
import com.coreintegration.database.entity.LegalEntity;
import com.coreintegration.database.mapper.LegalEntitiesMapper;
import com.coreintegration.database.repository.LegalEntitiesRepository;
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
public class LegalEntitiesCachedDatabaseServiceAware {

    private static final String CACHE_NAME = "legalEntitiesCache";
    private final CacheManager cacheManager;
    private final LegalEntitiesRepository legalEntitiesRepository;
    private final LegalEntitiesMapper legalEntitiesMapper;

    @NonNull
    public List<LegalEntityDto> getLegalEntitiesByAccountId(@NonNull final UUID accountId, @NonNull final List<UUID> idsToLoadFromCore, @NonNull final Supplier<Collection<LegalEntityDto>> supplier) {
        return getLegalEntitiesByAccountIds(List.of(accountId), idsToLoadFromCore, supplier);
    }

    @NonNull
    public List<LegalEntityDto> getLegalEntitiesByAccountIds(@NonNull final List<UUID> accountIds, @NonNull final List<UUID> idsToLoadFromCore, @NonNull final Supplier<Collection<LegalEntityDto>> supplier) {
        final List<LegalEntityDto> legalEntities = new ArrayList<>(accountIds.size());

        final List<UUID> idMissedFromCache = new ArrayList<>();
        idsToLoadFromCore.clear();
        final Cache legalEntitiesCache = cacheManager.getCache(CACHE_NAME);
        accountIds.forEach(accountId -> {
            if (legalEntitiesCache != null) {
                final LegalEntityDto legalEntity = legalEntitiesCache.get(accountId, LegalEntityDto.class);
                if (legalEntity != null) {
                    legalEntities.add(legalEntity);
                } else {
                    idMissedFromCache.add(accountId);
                }
            } else {
                idMissedFromCache.add(accountId);
            }
        });

        if (!idMissedFromCache.isEmpty()) {
            legalEntities.addAll(getLegalEntitiesListAndUpdateFromSupplier(idMissedFromCache, idsToLoadFromCore, supplier));
            if (legalEntitiesCache != null) {
                legalEntities.forEach(balance -> legalEntitiesCache.put(balance.getAccountId(), balance));
            }
        }

        return legalEntities;
    }

    private Collection<LegalEntityDto> getLegalEntitiesListAndUpdateFromSupplier(final List<UUID> accountIds, List<UUID> idsToLoad, final Supplier<Collection<LegalEntityDto>> supplier) {
        final List<LegalEntity> allByIdIn = legalEntitiesRepository.findAllByAccountIdIn(accountIds);
        final Map<UUID, LegalEntity> map = allByIdIn.stream().collect(Collectors.toMap(LegalEntity::getAccountId, Function.identity()));

        accountIds.stream()
                .filter(id -> !map.containsKey(id))
                .forEach(idsToLoad::add);
        final Collection<LegalEntityDto> legalEntitiesFromCore = supplier.get();
        if (!legalEntitiesFromCore .isEmpty()) {
            legalEntitiesRepository.saveAllAsync(legalEntitiesMapper.toEntityList(legalEntitiesFromCore));
        }
        legalEntitiesFromCore.addAll(legalEntitiesMapper.toDtoList(allByIdIn));

        return legalEntitiesFromCore;
    }
}

package com.coreintegration.database.service;


import com.coreintegration.commons.model.DebitCardDto;
import com.coreintegration.database.entity.DebitCard;
import com.coreintegration.database.mapper.DebitCardsMapper;
import com.coreintegration.database.repository.DebitCardsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DebitCardCachedDatabaseServiceAware {

    private static final String CACHE_NAME = "debitCardCache";
    private final CacheManager cacheManager;
    private final DebitCardsRepository debitCardsRepository;
    private final DebitCardsMapper debitCardsMapper;

    @Cacheable(value = CACHE_NAME, key = "#debitCardId", unless = "#result == null")
    public DebitCardDto getDebitCard(@NonNull final UUID debitCardId, @NonNull final Supplier<DebitCardDto> supplier) {
        return getDebitCardsAndUpdateFromSupplier(debitCardId, supplier);
    }
    @NonNull
    public List<DebitCardDto> getListOfDebitCard(@NonNull final List<UUID> debitCardIds, @NonNull final List<UUID> idsToLoadFromCore, @NonNull final Supplier<List<DebitCardDto>> supplier) {
        final List<DebitCardDto> details = new ArrayList<>(debitCardIds.size());

        final List<UUID> idMissedFromCache = new ArrayList<>();
        idsToLoadFromCore.clear();
        final Cache balancesCache = cacheManager.getCache(CACHE_NAME);
        debitCardIds.forEach(debitCardId -> {
            if (balancesCache != null) {
                final DebitCardDto debitCard = balancesCache.get(debitCardId, DebitCardDto.class);
                if (debitCard != null) {
                    details.add(debitCard);
                } else {
                    idMissedFromCache.add(debitCardId);
                }
            } else {
                idMissedFromCache.add(debitCardId);
            }
        });

        if (!idMissedFromCache.isEmpty()) {
            details.addAll(getDebitCardsListAndUpdateFromSupplier(idMissedFromCache, idsToLoadFromCore, supplier));
            if (balancesCache != null) {
                details.forEach(balance -> balancesCache.put(balance.getAccountId(), balance));
            }
        }

        return details;
    }

    private List<DebitCardDto> getDebitCardsListAndUpdateFromSupplier(final List<UUID> debitCardIds, List<UUID> idsToLoad, final Supplier<List<DebitCardDto>> supplier) {
        final List<DebitCard> allByIdIn = debitCardsRepository.findAllByAccountIdIn(debitCardIds);
        final Map<UUID, DebitCard> map = allByIdIn.stream().collect(Collectors.toMap(DebitCard::getAccountId, Function.identity()));

        debitCardIds.stream()
                .filter(id -> !map.containsKey(id))
                .forEach(idsToLoad::add);
        final List<DebitCardDto> detailsFromCore = supplier.get();
        if (!detailsFromCore .isEmpty()) {
            debitCardsRepository.saveAllAsync(debitCardsMapper.toEntityList(detailsFromCore));
        }
        detailsFromCore.addAll(debitCardsMapper.toDtoList(allByIdIn));

        return detailsFromCore;
    }

    private DebitCardDto getDebitCardsAndUpdateFromSupplier(final UUID debitCardId, final Supplier<DebitCardDto> supplier) {
        final DebitCard entity = loadOrGetFromSupplier(debitCardId, supplier);
        return debitCardsMapper.toDto(entity);
    }

    private DebitCard loadOrGetFromSupplier(final UUID debitCardId, final Supplier<DebitCardDto> supplier) {
        return debitCardsRepository.findById(debitCardId)
                .orElseGet(() -> debitCardsMapper.toEntity(supplier.get()));
    }
}

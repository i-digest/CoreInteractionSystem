package com.coreintegration.database.service;


import com.coreintegration.commons.model.DebitCardDto;
import com.coreintegration.database.entity.DebitCard;
import com.coreintegration.database.mapper.DebitCardsMapper;
import com.coreintegration.database.repository.DebitCardsRepository;
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
public class DebitCardCachedDatabaseServiceAware {

    private static final String CACHE_NAME = "debitCardCache";
    private final CacheManager cacheManager;
    private final DebitCardsRepository debitCardsRepository;
    private final DebitCardsMapper debitCardsMapper;

    @NonNull
    public List<DebitCardDto> getDebitCard(@NonNull final UUID debitCardId, @NonNull final List<UUID> idsToLoadFromCore, @NonNull final Supplier<Collection<DebitCardDto>> supplier) {
        return getListOfDebitCard(List.of(debitCardId), idsToLoadFromCore, supplier);
    }

    @NonNull
    public List<DebitCardDto> getListOfDebitCard(@NonNull final List<UUID> debitCardIds, @NonNull final List<UUID> idsToLoadFromCore, @NonNull final Supplier<Collection<DebitCardDto>> supplier) {
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

    private Collection<DebitCardDto> getDebitCardsListAndUpdateFromSupplier(final List<UUID> debitCardIds, List<UUID> idsToLoad, final Supplier<Collection<DebitCardDto>> supplier) {
        final List<DebitCard> allByIdIn = debitCardsRepository.findAllByAccountIdIn(debitCardIds);
        final Map<UUID, DebitCard> map = allByIdIn.stream().collect(Collectors.toMap(DebitCard::getAccountId, Function.identity()));

        debitCardIds.stream()
                .filter(id -> !map.containsKey(id))
                .forEach(idsToLoad::add);
        final Collection<DebitCardDto> detailsFromCore = supplier.get();
        if (!detailsFromCore .isEmpty()) {
            debitCardsRepository.saveAllAsync(debitCardsMapper.toEntityList(detailsFromCore));
        }
        detailsFromCore.addAll(debitCardsMapper.toDtoList(allByIdIn));

        return detailsFromCore;
    }
}

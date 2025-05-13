package com.coreintegration.database.service;

import com.coreintegration.commons.model.LoanDto;
import com.coreintegration.database.entity.Loan;
import com.coreintegration.database.mapper.LoansMapper;
import com.coreintegration.database.repository.LoansRepository;
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
public class LoansCachedDatabaseServiceAware {

    private static final String CACHE_NAME = "loansCache";
    private final CacheManager cacheManager;
    private final LoansRepository loansRepository;
    private final LoansMapper loansMapper;

    @NonNull
    public List<LoanDto> getLoansByAccountId(@NonNull final UUID accountId, @NonNull final List<UUID> idsToLoadFromCore, @NonNull final Supplier<Collection<LoanDto>> supplier) {
        return getLoansByAccountIds(List.of(accountId), idsToLoadFromCore, supplier);
    }

    @NonNull
    public List<LoanDto> getLoansByAccountIds(@NonNull final List<UUID> accountIds, @NonNull final List<UUID> idsToLoadFromCore, @NonNull final Supplier<Collection<LoanDto>> supplier) {
        final List<LoanDto> loans = new ArrayList<>(accountIds.size());

        final List<UUID> idMissedFromCache = new ArrayList<>();
        idsToLoadFromCore.clear();
        final Cache loansCache = cacheManager.getCache(CACHE_NAME);
        accountIds.forEach(accountId -> {
            if (loansCache != null) {
                final LoanDto loan = loansCache.get(accountId, LoanDto.class);
                if (loan != null) {
                    loans.add(loan);
                } else {
                    idMissedFromCache.add(accountId);
                }
            } else {
                idMissedFromCache.add(accountId);
            }
        });

        if (!idMissedFromCache.isEmpty()) {
            loans.addAll(getLimitsListAndUpdateFromSupplier(idMissedFromCache, idsToLoadFromCore, supplier));
            if (loansCache != null) {
                loans.forEach(balance -> loansCache.put(balance.getAccountId(), balance));
            }
        }

        return loans;
    }

    private Collection<LoanDto> getLimitsListAndUpdateFromSupplier(final List<UUID> accountIds, List<UUID> idsToLoad, final Supplier<Collection<LoanDto>> supplier) {
        final List<Loan> allByIdIn = loansRepository.findAllByAccountIdIn(accountIds);
        final Map<UUID, Loan> map = allByIdIn.stream().collect(Collectors.toMap(Loan::getAccountId, Function.identity()));

        accountIds.stream()
                .filter(id -> !map.containsKey(id))
                .forEach(idsToLoad::add);
        final Collection<LoanDto> loansFromCore = supplier.get();
        if (!loansFromCore .isEmpty()) {
            loansRepository.saveAllAsync(loansMapper.toEntityList(loansFromCore));
        }
        loansFromCore.addAll(loansMapper.toDtoList(allByIdIn));

        return loansFromCore;
    }
}

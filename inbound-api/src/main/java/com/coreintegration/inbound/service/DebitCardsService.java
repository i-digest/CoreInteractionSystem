package com.coreintegration.inbound.service;

import com.coreintegration.commons.model.DebitCardDto;
import com.coreintegration.commons.model.DebitCardListResponseDto;
import com.coreintegration.commons.model.DebitCardResponseDto;
import com.coreintegration.database.service.DebitCardCachedDatabaseServiceAware;
import com.coreintegration.outbound.client.DebitCardsClient;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DebitCardsService {

    private final DebitCardCachedDatabaseServiceAware cacheServiceAware;
    private final DebitCardsClient debitCardsClient;

    @RateLimiter(name = "debitCardsRateLimiter")
    public DebitCardResponseDto getDebitCardsByAccountId(@NonNull final UUID accountId) {
        final List<UUID> idsToFetchFromCore = new ArrayList<>();
        final List<DebitCardDto> debitCards = cacheServiceAware.getDebitCard(accountId, idsToFetchFromCore, () -> debitCardsClient.getDebitCardsFromCore(accountId));

        return new DebitCardResponseDto().debitCards(debitCards);
    }

    @RateLimiter(name = "debitCardsBulkRateLimiter")
    public DebitCardListResponseDto getDebitCardsByAccountIds(@NonNull final List<UUID> accountIds) {
        final List<UUID> idsToFetchFromCore = new ArrayList<>();
        final List<DebitCardDto> debitCards = cacheServiceAware.getListOfDebitCard(accountIds, idsToFetchFromCore, () -> debitCardsClient.getListOfDebitCardsFromCore(idsToFetchFromCore));
        final Map<String, List<DebitCardDto>> debitCardsMap = debitCards.stream()
                .collect(Collectors.groupingBy(dc -> dc.getId().toString()));

        return new DebitCardListResponseDto().debitCards(debitCardsMap);
    }
}

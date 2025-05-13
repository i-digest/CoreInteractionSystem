package com.coreintegration.inbound.service;

import com.coreintegration.commons.model.DebitCardListResponseDto;
import com.coreintegration.commons.model.DebitCardResponseDto;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DebitCardsService {

    @RateLimiter(name = "debitCardsRateLimiter")
    public DebitCardResponseDto getDebitCardsByAccountId(final UUID accountId) {
        return null;
    }

    @RateLimiter(name = "debitCardsBulkRateLimiter")
    public DebitCardListResponseDto getDebitCardsByAccountIds(final List<UUID> accountIds) {
        return null;
    }
}

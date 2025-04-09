package com.coreintegration.inbound.service;

import com.coreintegration.commons.model.DebitCardListResponse;
import com.coreintegration.commons.model.DebitCardResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DebitCardsService {

    @RateLimiter(name = "debitCardsRateLimiter")
    public DebitCardResponse getById(String id) {
        return null;
    }

    @RateLimiter(name = "debitCardsBulkRateLimiter")
    public DebitCardListResponse getByIds(List<String> ids) {
        return null;
    }
}

package com.coreintegration.inbound.service;

import com.coreintegration.commons.model.LoansListResponseDto;
import com.coreintegration.commons.model.LoansResponseDto;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoansService {

    @RateLimiter(name = "loansRateLimiter")
    public LoansResponseDto getLoansByAccountId(final UUID accountId) {
        return null;
    }

    @RateLimiter(name = "loansBulkRateLimiter")
    public LoansListResponseDto getLoansByAccountIds(final List<UUID> accountIds) {
        return null;
    }
}

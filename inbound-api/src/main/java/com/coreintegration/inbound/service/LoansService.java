package com.coreintegration.inbound.service;

import com.coreintegration.commons.model.LoanDto;
import com.coreintegration.commons.model.LoansListResponseDto;
import com.coreintegration.commons.model.LoansResponseDto;
import com.coreintegration.database.service.LoansCachedDatabaseServiceAware;
import com.coreintegration.outbound.client.LoansClient;
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
public class LoansService {

    private final LoansCachedDatabaseServiceAware cacheServiceAware;
    private final LoansClient loansClient;

    @RateLimiter(name = "loansRateLimiter")
    public LoansResponseDto getLoansByAccountId(@NonNull final UUID accountId) {
        final List<UUID> idsToFetchFromCore = new ArrayList<>();
        final List<LoanDto> limits = cacheServiceAware.getLoansByAccountId(accountId, idsToFetchFromCore, () -> loansClient.getLoansByAccountId(accountId));

        return new LoansResponseDto().loans(limits);
    }

    @RateLimiter(name = "loansBulkRateLimiter")
    public LoansListResponseDto getLoansByAccountIds(@NonNull final List<UUID> accountIds) {
        final List<UUID> idsToFetchFromCore = new ArrayList<>();
        final List<LoanDto> limits = cacheServiceAware.getLoansByAccountIds(accountIds, idsToFetchFromCore, () -> loansClient.getLoansByAccountIds(idsToFetchFromCore));
        final Map<String, List<LoanDto>> limitsMap = limits.stream()
                .collect(Collectors.groupingBy(l -> l.getId().toString()));

        return new LoansListResponseDto().loans(limitsMap);
    }
}

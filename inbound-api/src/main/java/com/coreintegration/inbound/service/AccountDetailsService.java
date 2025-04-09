package com.coreintegration.inbound.service;


import com.coreintegration.commons.model.AccountDetails;
import com.coreintegration.commons.model.AccountDetailsListResponse;
import com.coreintegration.commons.model.AccountDetailsResponse;
import com.coreintegration.database.service.CachedDatabaseServiceAware;
import com.coreintegration.outbound.client.AccountDetailsClient;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountDetailsService {

    private final CachedDatabaseServiceAware cacheServiceAware;
    private final AccountDetailsClient accountDetailsClient;

    @RateLimiter(name = "accountsRateLimiter")
    public AccountDetailsResponse getAccountDetailsById(@NonNull final String accountId) {
        AccountDetails accountDetails = cacheServiceAware.getAccountDetails(accountId,
                () -> accountDetailsClient.getDetailsFromCore(accountId));

        return new AccountDetailsResponse().accountDetails(accountDetails);
    }

    @RateLimiter(name = "accountsBulkRateLimiter")
    public AccountDetailsListResponse getAccountsDetailsByIds(@NonNull final List<String> accountIds) {
        return null;
    }
}

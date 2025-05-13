package com.coreintegration.inbound.service;


import com.coreintegration.commons.model.AccountDetailsDto;
import com.coreintegration.commons.model.AccountDetailsListResponseDto;
import com.coreintegration.commons.model.AccountDetailsResponseDto;
import com.coreintegration.database.service.AccountDetailsCachedDatabaseServiceAware;
import com.coreintegration.outbound.client.AccountDetailsClient;
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
public class AccountDetailsService {

    private final AccountDetailsCachedDatabaseServiceAware cacheServiceAware;
    private final AccountDetailsClient accountDetailsClient;

    @RateLimiter(name = "accountsRateLimiter")
    public AccountDetailsResponseDto getAccountDetailsById(@NonNull final UUID accountId) {
        AccountDetailsDto accountDetails = cacheServiceAware.getAccountDetails(accountId,
                () -> accountDetailsClient.getDetailsFromCore(accountId));

        return new AccountDetailsResponseDto().accountDetails(accountDetails);
    }

    @RateLimiter(name = "accountsBulkRateLimiter")
    public AccountDetailsListResponseDto getAccountsDetailsByIds(@NonNull final List<UUID> accountIds) {
        final List<UUID> idsToFetchFromCore = new ArrayList<>();
        final List<AccountDetailsDto> accountDetails = cacheServiceAware.getListOfAccountDetails(accountIds, idsToFetchFromCore, () -> accountDetailsClient.getListOfDetailsFromCore(idsToFetchFromCore));
        final Map<String, AccountDetailsDto> accountDetailsMap = accountDetails.stream()
                .collect(Collectors.toMap(accountDetail -> accountDetail.getId().toString(), accountDetail -> accountDetail, (a, b) -> b));

        return new AccountDetailsListResponseDto().accountDetails(accountDetailsMap);
    }

}

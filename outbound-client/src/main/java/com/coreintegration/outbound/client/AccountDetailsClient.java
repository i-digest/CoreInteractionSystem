package com.coreintegration.outbound.client;

import com.coreintegration.circuitbreaker.CircuitBreakerExecutor;
import com.coreintegration.commons.model.AccountDetailsDto;
import com.coreintegration.fallbackengine.service.AccountDetailsFallbackService;
import com.coreintegration.outbound.client.api.AccountDetailsApi;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountDetailsClient {

    private final AccountDetailsApi api = new AccountDetailsApi();
    private final CircuitBreakerExecutor circuitBreakerExecutor;
    private final AccountDetailsFallbackService fallbackService;

    @NonNull
    public AccountDetailsDto getAccountDetailsById(@NotBlank final UUID accountId) {
        return circuitBreakerExecutor.run("getAccountDetailsByAccountIds",
                () -> api.getAccountDetailsById(accountId).getAccountDetails(),
                () -> fallbackService.getFallbackByAccountId(accountId));
    }

    @NonNull
    public Collection<AccountDetailsDto> getAccountsDetailsByIds(@NonNull final List<UUID> accountIds) {
        return circuitBreakerExecutor.run(
                        "getAccountDetailsByAccountIds",
                        () -> api.getAccountsDetailsByIds(accountIds).getAccountDetails(),
                        () -> fallbackService.getFallbackByAccountIds(accountIds))
                .values();
    }
}

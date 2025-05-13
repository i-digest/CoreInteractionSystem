package com.coreintegration.outbound.client;

import com.coreintegration.commons.model.AccountDetailsDto;
import com.coreintegration.commons.model.AccountDetailsListResponseDto;
import com.coreintegration.commons.model.AccountDetailsResponseDto;
import com.coreintegration.outbound.client.api.AccountDetailsApi;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
public class AccountDetailsClient {

    private final AccountDetailsApi api = new AccountDetailsApi();

    @NonNull
    public AccountDetailsDto getAccountDetailsById(@NotBlank final UUID accountId) {
        final AccountDetailsResponseDto responseDto = api.getAccountDetailsById(accountId);
        return responseDto.getAccountDetails();
    }

    @NonNull
    public Collection<AccountDetailsDto> getAccountsDetailsByIds(@NonNull final List<UUID> accountIds) {
        final AccountDetailsListResponseDto responseDto = api.getAccountsDetailsByIds(accountIds);
        return responseDto.getAccountDetails().values();
    }
}

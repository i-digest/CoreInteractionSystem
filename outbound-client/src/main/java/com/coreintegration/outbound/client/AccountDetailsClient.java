package com.coreintegration.outbound.client;

import com.coreintegration.circuitbreaker.CircuitBreakerExecutor;
import com.coreintegration.commons.model.AccountDetailsDto;
import com.coreintegration.database.mapper.AccountDetailsMapper;
import com.coreintegration.database.repository.AccountDetailsRepository;
import com.coreintegration.outbound.client.api.AccountDetailsApi;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccountDetailsClient {

    private final AccountDetailsApi api = new AccountDetailsApi();
    private final CircuitBreakerExecutor circuitBreakerExecutor;
    private final AccountDetailsRepository accountDetailsRepository;
    private final AccountDetailsMapper accountDetailsMapper;

    @NonNull
    public AccountDetailsDto getAccountDetailsById(@NotBlank final UUID accountId) {
        return circuitBreakerExecutor.run("getAccountDetailsByAccountIds",
                () -> api.getAccountDetailsById(accountId).getAccountDetails(),
                () -> accountDetailsMapper.toDto(accountDetailsRepository.findById(accountId)
                        .orElseThrow(() -> new NoSuchElementException("Account details not found"))));
    }

    @NonNull
    public Collection<AccountDetailsDto> getAccountsDetailsByIds(@NonNull final List<UUID> accountIds) {
        return circuitBreakerExecutor.run(
                "getAccountDetailsByAccountIds",
                () -> api.getAccountsDetailsByIds(accountIds).getAccountDetails(),
                () -> accountDetailsRepository.findAllByIdIn(accountIds).stream()
                        .map(accountDetailsMapper::toDto)
                        .collect(Collectors.toMap(accountDetailsDto -> accountDetailsDto.getId().toString(), accountDetailsDto -> accountDetailsDto))
        ).values();
    }
}

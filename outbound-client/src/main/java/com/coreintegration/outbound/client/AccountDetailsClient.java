package com.coreintegration.outbound.client;

import com.coreintegration.commons.model.AccountDetailsDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AccountDetailsClient {

    public AccountDetailsDto getDetailsFromCore(UUID id) {
        return new AccountDetailsDto();
    }

    public List<AccountDetailsDto> getListOfDetailsFromCore(List<UUID> supplier) {
        return List.of(new AccountDetailsDto());
    }
}

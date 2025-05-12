package com.coreintegration.outbound.client;

import com.coreintegration.commons.model.AccountDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountDetailsClient {

    public AccountDetails getDetailsFromCore(String id) {
        return new AccountDetails();
    }

    public List<AccountDetails> getListOfDetailsFromCore(List<String> supplier) {
        return List.of(new AccountDetails());
    }
}

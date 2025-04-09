package com.coreintegration.outbound.client;

import com.coreintegration.commons.model.AccountDetails;
import org.springframework.stereotype.Component;

@Component
public class AccountDetailsClient {

    public AccountDetails getDetailsFromCore(String id) {
        return new AccountDetails();
    }
}

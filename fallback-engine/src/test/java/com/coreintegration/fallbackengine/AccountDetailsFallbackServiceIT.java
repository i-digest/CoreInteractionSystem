package com.coreintegration.fallbackengine;

import com.coreintegration.commons.model.AccountDetailsDto;
import com.coreintegration.fallbackengine.service.AccountDetailsFallbackService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AccountDetailsFallbackServiceIT {

    @Autowired
    private AccountDetailsFallbackService service;

    @Test
    void shouldReturnMapForAccountIds() {
        Map<String, AccountDetailsDto> result = service.getFallbackByAccountIds(Collections.singletonList(UUID.randomUUID()));
        assertNotNull(result);
        // The map might be empty if no data exists in the test database
        assertTrue(result != null);
    }
}
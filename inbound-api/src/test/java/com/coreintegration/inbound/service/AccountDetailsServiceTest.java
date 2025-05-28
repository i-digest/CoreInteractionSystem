package com.coreintegration.inbound.service;

import com.coreintegration.commons.model.AccountDetailsDto;
import com.coreintegration.commons.model.AccountDetailsListResponseDto;
import com.coreintegration.commons.model.AccountDetailsResponseDto;
import com.coreintegration.database.service.AccountDetailsCachedDatabaseServiceAware;
import com.coreintegration.outbound.client.AccountDetailsClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountDetailsServiceTest {

    @Mock
    private AccountDetailsCachedDatabaseServiceAware cacheServiceAware;

    @Mock
    private AccountDetailsClient accountDetailsClient;

    @InjectMocks
    private AccountDetailsService accountDetailsService;

    private UUID accountId;
    private List<UUID> accountIds;
    private AccountDetailsDto accountDetailsDto;
    private List<AccountDetailsDto> accountDetailsDtos;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        accountIds = List.of(accountId, UUID.randomUUID());

        // Setup account details DTO
        accountDetailsDto = new AccountDetailsDto().id(accountId);

        // Setup collection of account details DTOs
        accountDetailsDtos = List.of(
            accountDetailsDto,
            new AccountDetailsDto().id(UUID.randomUUID())
        );
    }

    @Test
    void getAccountDetailsById_ShouldReturnAccountDetailsForSingleAccount() {
        // Arrange
        when(cacheServiceAware.getAccountDetailsById(eq(accountId), any(Supplier.class)))
            .thenReturn(accountDetailsDto);

        // Act
        AccountDetailsResponseDto response = accountDetailsService.getAccountDetailsById(accountId);

        // Assert
        assertNotNull(response);
        assertEquals(accountDetailsDto, response.getAccountDetails());
        verify(cacheServiceAware, times(1)).getAccountDetailsById(eq(accountId), any(Supplier.class));
    }

    @Test
    void getAccountsDetailsByIds_ShouldReturnAccountDetailsForMultipleAccounts() {
        // Arrange
        when(cacheServiceAware.getAccountDetailsByIds(eq(accountIds), any(ArrayList.class), any(Supplier.class)))
            .thenReturn(accountDetailsDtos);

        // Act
        AccountDetailsListResponseDto response = accountDetailsService.getAccountsDetailsByIds(accountIds);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getAccountDetails());

        // Verify the map contains entries for each account
        for (AccountDetailsDto account : accountDetailsDtos) {
            String accountIdStr = account.getId().toString();
            assertTrue(response.getAccountDetails().containsKey(accountIdStr));
        }

        verify(cacheServiceAware, times(1)).getAccountDetailsByIds(eq(accountIds), any(ArrayList.class), any(Supplier.class));
    }
}

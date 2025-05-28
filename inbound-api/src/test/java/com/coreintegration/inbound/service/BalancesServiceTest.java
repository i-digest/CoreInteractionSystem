package com.coreintegration.inbound.service;

import com.coreintegration.commons.model.BalanceDto;
import com.coreintegration.commons.model.BalanceListResponseDto;
import com.coreintegration.commons.model.BalanceResponseDto;
import com.coreintegration.database.service.BalancesCachedDatabaseServiceAware;
import com.coreintegration.outbound.client.BalanceClient;
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
class BalancesServiceTest {

    @Mock
    private BalancesCachedDatabaseServiceAware cacheServiceAware;

    @Mock
    private BalanceClient balanceClient;

    @InjectMocks
    private BalancesService balancesService;

    private UUID accountId;
    private List<UUID> accountIds;
    private List<BalanceDto> balanceDtos;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        accountIds = List.of(accountId, UUID.randomUUID());

        // Setup balance DTOs
        balanceDtos = List.of(
            new BalanceDto().id(UUID.randomUUID()),
            new BalanceDto().id(UUID.randomUUID())
        );
    }

    @Test
    void getBalanceById_ShouldReturnBalancesForSingleAccount() {
        // Arrange
        when(cacheServiceAware.getBalancesByAccountId(eq(accountId), any(ArrayList.class), any(Supplier.class)))
            .thenReturn(balanceDtos);

        // Act
        BalanceResponseDto response = balancesService.getBalanceById(accountId);

        // Assert
        assertNotNull(response);
        assertEquals(balanceDtos, response.getBalances());
        verify(cacheServiceAware, times(1)).getBalancesByAccountId(eq(accountId), any(ArrayList.class), any(Supplier.class));
    }

    @Test
    void getBalancesByIds_ShouldReturnBalancesForMultipleAccounts() {
        // Arrange
        when(cacheServiceAware.getBalancesByAccountIds(eq(accountIds), any(ArrayList.class), any(Supplier.class)))
            .thenReturn(balanceDtos);

        // Act
        BalanceListResponseDto response = balancesService.getBalancesByIds(accountIds);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getBalances());
        
        // Verify the map contains entries for each balance
        for (BalanceDto balance : balanceDtos) {
            String balanceId = balance.getId().toString();
            assertTrue(response.getBalances().containsKey(balanceId));
        }
        
        verify(cacheServiceAware, times(1)).getBalancesByAccountIds(eq(accountIds), any(ArrayList.class), any(Supplier.class));
    }
}
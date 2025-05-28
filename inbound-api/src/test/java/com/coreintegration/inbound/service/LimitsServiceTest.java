package com.coreintegration.inbound.service;

import com.coreintegration.commons.model.LimitDto;
import com.coreintegration.commons.model.LimitsListResponseDto;
import com.coreintegration.commons.model.LimitsResponseDto;
import com.coreintegration.database.service.LimitsCachedDatabaseServiceAware;
import com.coreintegration.outbound.client.LimitsClient;
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
class LimitsServiceTest {

    @Mock
    private LimitsCachedDatabaseServiceAware cacheServiceAware;

    @Mock
    private LimitsClient limitsClient;

    @InjectMocks
    private LimitsService limitsService;


    private UUID accountId;
    private List<UUID> accountIds;
    private List<LimitDto> limitDtos;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        accountIds = List.of(accountId, UUID.randomUUID());

        // Setup limit DTOs
        limitDtos = List.of(
            new LimitDto().id(UUID.randomUUID()),
            new LimitDto().id(UUID.randomUUID())
        );
    }

    @Test
    void getLimitsByAccountId_ShouldReturnLimitsForSingleAccount() {
        // Arrange
        when(cacheServiceAware.getLimitsByAccountId(eq(accountId), any(ArrayList.class), any(Supplier.class)))
            .thenReturn(limitDtos);

        // Act
        LimitsResponseDto response = limitsService.getLimitsByAccountId(accountId);

        // Assert
        assertNotNull(response);
        assertEquals(limitDtos, response.getLimits());
        verify(cacheServiceAware, times(1)).getLimitsByAccountId(eq(accountId), any(ArrayList.class), any(Supplier.class));
    }

    @Test
    void getLimitsByAccountIds_ShouldReturnLimitsForMultipleAccounts() {
        // Arrange
        when(cacheServiceAware.getLimitsByAccountIds(eq(accountIds), any(ArrayList.class), any(Supplier.class)))
            .thenReturn(limitDtos);

        // Act
        LimitsListResponseDto response = limitsService.getLimitsByAccountIds(accountIds);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getLimits());
        // Verify the map contains entries for each limit
        for (LimitDto limit : limitDtos) {
            String limitId = limit.getId().toString();
            assertTrue(response.getLimits().containsKey(limitId));
        }
        verify(cacheServiceAware, times(1)).getLimitsByAccountIds(eq(accountIds), any(ArrayList.class), any(Supplier.class));
    }
}

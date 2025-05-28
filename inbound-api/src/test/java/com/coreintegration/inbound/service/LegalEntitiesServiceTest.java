package com.coreintegration.inbound.service;

import com.coreintegration.commons.model.LegalEntitiesListResponseDto;
import com.coreintegration.commons.model.LegalEntitiesResponseDto;
import com.coreintegration.commons.model.LegalEntityDto;
import com.coreintegration.database.service.LegalEntitiesCachedDatabaseServiceAware;
import com.coreintegration.outbound.client.LegalEntitiesClient;
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
class LegalEntitiesServiceTest {

    @Mock
    private LegalEntitiesCachedDatabaseServiceAware cacheServiceAware;

    @Mock
    private LegalEntitiesClient legalEntitiesClient;

    @InjectMocks
    private LegalEntitiesService legalEntitiesService;

    private UUID accountId;
    private List<UUID> accountIds;
    private List<LegalEntityDto> legalEntityDtos;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        accountIds = List.of(accountId, UUID.randomUUID());

        // Setup legal entity DTOs
        legalEntityDtos = List.of(
            new LegalEntityDto().id(UUID.randomUUID()),
            new LegalEntityDto().id(UUID.randomUUID())
        );
    }

    @Test
    void getLegalEntitiesByAccountId_ShouldReturnLegalEntitiesForSingleAccount() {
        // Arrange
        when(cacheServiceAware.getLegalEntitiesByAccountId(eq(accountId), any(ArrayList.class), any(Supplier.class)))
            .thenReturn(legalEntityDtos);

        // Act
        LegalEntitiesResponseDto response = legalEntitiesService.getLegalEntitiesByAccountId(accountId);

        // Assert
        assertNotNull(response);
        assertEquals(legalEntityDtos, response.getLegalEntities());
        verify(cacheServiceAware, times(1)).getLegalEntitiesByAccountId(eq(accountId), any(ArrayList.class), any(Supplier.class));
    }

    @Test
    void getLegalEntitiesByAccountIds_ShouldReturnLegalEntitiesForMultipleAccounts() {
        // Arrange
        when(cacheServiceAware.getLegalEntitiesByAccountIds(eq(accountIds), any(ArrayList.class), any(Supplier.class)))
            .thenReturn(legalEntityDtos);

        // Act
        LegalEntitiesListResponseDto response = legalEntitiesService.getLegalEntitiesByAccountIds(accountIds);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getLegalEntities());
        
        // Verify the map contains entries for each legal entity
        for (LegalEntityDto legalEntity : legalEntityDtos) {
            String legalEntityId = legalEntity.getId().toString();
            assertTrue(response.getLegalEntities().containsKey(legalEntityId));
        }
        
        verify(cacheServiceAware, times(1)).getLegalEntitiesByAccountIds(eq(accountIds), any(ArrayList.class), any(Supplier.class));
    }
}
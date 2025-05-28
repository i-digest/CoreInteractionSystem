package com.coreintegration.inbound.controllers;

import com.coreintegration.commons.model.LegalEntitiesListResponseDto;
import com.coreintegration.commons.model.LegalEntitiesResponseDto;
import com.coreintegration.commons.model.LegalEntityDto;
import com.coreintegration.inbound.service.LegalEntitiesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LegalEntitiesControllerTest {

    @Mock
    private LegalEntitiesService legalEntitiesService;

    @InjectMocks
    private LegalEntitiesController legalEntitiesController;

    private UUID accountId;
    private List<UUID> accountIds;
    private LegalEntitiesResponseDto legalEntitiesResponseDto;
    private LegalEntitiesListResponseDto legalEntitiesListResponseDto;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        accountIds = List.of(accountId, UUID.randomUUID());
        
        // Setup single account response
        legalEntitiesResponseDto = new LegalEntitiesResponseDto();
        legalEntitiesResponseDto.setLegalEntities(List.of(new LegalEntityDto().id(UUID.randomUUID())));
        
        // Setup multiple accounts response
        legalEntitiesListResponseDto = new LegalEntitiesListResponseDto();
        Map<String, List<LegalEntityDto>> legalEntitiesMap = Map.of(
            accountId.toString(), List.of(new LegalEntityDto().id(UUID.randomUUID()))
        );
        legalEntitiesListResponseDto.setLegalEntities(legalEntitiesMap);
    }

    @Test
    void getLegalEntitiesByAccountId_ShouldReturnLegalEntitiesForSingleAccount() {
        // Arrange
        when(legalEntitiesService.getLegalEntitiesByAccountId(accountId)).thenReturn(legalEntitiesResponseDto);

        // Act
        ResponseEntity<LegalEntitiesResponseDto> response = legalEntitiesController.getLegalEntitiesByAccountId(accountId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(legalEntitiesResponseDto, response.getBody());
        verify(legalEntitiesService, times(1)).getLegalEntitiesByAccountId(accountId);
    }

    @Test
    void getLegalEntitiesByAccountIds_ShouldReturnLegalEntitiesForMultipleAccounts() {
        // Arrange
        when(legalEntitiesService.getLegalEntitiesByAccountIds(accountIds)).thenReturn(legalEntitiesListResponseDto);

        // Act
        ResponseEntity<LegalEntitiesListResponseDto> response = legalEntitiesController.getLegalEntitiesByAccountIds(accountIds);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(legalEntitiesListResponseDto, response.getBody());
        verify(legalEntitiesService, times(1)).getLegalEntitiesByAccountIds(accountIds);
    }
}
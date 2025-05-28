package com.coreintegration.inbound.controllers;

import com.coreintegration.commons.model.LimitDto;
import com.coreintegration.commons.model.LimitsListResponseDto;
import com.coreintegration.commons.model.LimitsResponseDto;
import com.coreintegration.inbound.service.LimitsService;
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
class LimitsControllerTest {

    @Mock
    private LimitsService limitsService;

    @InjectMocks
    private LimitsController limitsController;

    private UUID accountId;
    private List<UUID> accountIds;
    private LimitsResponseDto limitsResponseDto;
    private LimitsListResponseDto limitsListResponseDto;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        accountIds = List.of(accountId, UUID.randomUUID());
        
        // Setup single account response
        limitsResponseDto = new LimitsResponseDto();
        limitsResponseDto.setLimits(List.of(new LimitDto().id(UUID.randomUUID())));
        
        // Setup multiple accounts response
        limitsListResponseDto = new LimitsListResponseDto();
        Map<String, List<LimitDto>> limitsMap = Map.of(
            accountId.toString(), List.of(new LimitDto().id(UUID.randomUUID()))
        );
        limitsListResponseDto.setLimits(limitsMap);
    }

    @Test
    void getLimitsByAccountId_ShouldReturnLimitsForSingleAccount() {
        // Arrange
        when(limitsService.getLimitsByAccountId(accountId)).thenReturn(limitsResponseDto);

        // Act
        ResponseEntity<LimitsResponseDto> response = limitsController.getLimitsByAccountId(accountId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(limitsResponseDto, response.getBody());
        verify(limitsService, times(1)).getLimitsByAccountId(accountId);
    }

    @Test
    void getLimitsByAccountIds_ShouldReturnLimitsForMultipleAccounts() {
        // Arrange
        when(limitsService.getLimitsByAccountIds(accountIds)).thenReturn(limitsListResponseDto);

        // Act
        ResponseEntity<LimitsListResponseDto> response = limitsController.getLimitsByAccountIds(accountIds);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(limitsListResponseDto, response.getBody());
        verify(limitsService, times(1)).getLimitsByAccountIds(accountIds);
    }
}
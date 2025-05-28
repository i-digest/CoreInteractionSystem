package com.coreintegration.inbound.controllers;

import com.coreintegration.commons.model.BalanceDto;
import com.coreintegration.commons.model.BalanceListResponseDto;
import com.coreintegration.commons.model.BalanceResponseDto;
import com.coreintegration.inbound.service.BalancesService;
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
class BalancesControllerTest {

    @Mock
    private BalancesService service;

    @InjectMocks
    private BalancesController balancesController;

    private UUID accountId;
    private List<UUID> accountIds;
    private BalanceResponseDto balanceResponseDto;
    private BalanceListResponseDto balanceListResponseDto;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        accountIds = List.of(accountId, UUID.randomUUID());
        
        // Setup single account response
        balanceResponseDto = new BalanceResponseDto();
        balanceResponseDto.setBalances(List.of(new BalanceDto().id(UUID.randomUUID())));
        
        // Setup multiple accounts response
        balanceListResponseDto = new BalanceListResponseDto();
        Map<String, List<BalanceDto>> balancesMap = Map.of(
            accountId.toString(), List.of(new BalanceDto().id(UUID.randomUUID()))
        );
        balanceListResponseDto.setBalances(balancesMap);
    }

    @Test
    void getBalanceByAccountId_ShouldReturnBalancesForSingleAccount() {
        // Arrange
        when(service.getBalanceById(accountId)).thenReturn(balanceResponseDto);

        // Act
        ResponseEntity<BalanceResponseDto> response = balancesController.getBalanceByAccountId(accountId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(balanceResponseDto, response.getBody());
        verify(service, times(1)).getBalanceById(accountId);
    }

    @Test
    void getBalancesByAccountIds_ShouldReturnBalancesForMultipleAccounts() {
        // Arrange
        when(service.getBalancesByIds(accountIds)).thenReturn(balanceListResponseDto);

        // Act
        ResponseEntity<BalanceListResponseDto> response = balancesController.getBalancesByAccountIds(accountIds);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(balanceListResponseDto, response.getBody());
        verify(service, times(1)).getBalancesByIds(accountIds);
    }
}
package com.coreintegration.inbound.controllers;

import com.coreintegration.commons.model.LoanDto;
import com.coreintegration.commons.model.LoansListResponseDto;
import com.coreintegration.commons.model.LoansResponseDto;
import com.coreintegration.inbound.service.LoansService;
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
class LoansControllerTest {

    @Mock
    private LoansService loansService;

    @InjectMocks
    private LoansController loansController;

    private UUID accountId;
    private List<UUID> accountIds;
    private LoansResponseDto loansResponseDto;
    private LoansListResponseDto loansListResponseDto;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        accountIds = List.of(accountId, UUID.randomUUID());
        
        // Setup single account response
        loansResponseDto = new LoansResponseDto();
        loansResponseDto.setLoans(List.of(new LoanDto().id(UUID.randomUUID())));
        
        // Setup multiple accounts response
        loansListResponseDto = new LoansListResponseDto();
        Map<String, List<LoanDto>> loansMap = Map.of(
            accountId.toString(), List.of(new LoanDto().id(UUID.randomUUID()))
        );
        loansListResponseDto.setLoans(loansMap);
    }

    @Test
    void getLoansByAccountId_ShouldReturnLoansForSingleAccount() {
        // Arrange
        when(loansService.getLoansByAccountId(accountId)).thenReturn(loansResponseDto);

        // Act
        ResponseEntity<LoansResponseDto> response = loansController.getLoansByAccountId(accountId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loansResponseDto, response.getBody());
        verify(loansService, times(1)).getLoansByAccountId(accountId);
    }

    @Test
    void getLoansByAccountIds_ShouldReturnLoansForMultipleAccounts() {
        // Arrange
        when(loansService.getLoansByAccountIds(accountIds)).thenReturn(loansListResponseDto);

        // Act
        ResponseEntity<LoansListResponseDto> response = loansController.getLoansByAccountIds(accountIds);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loansListResponseDto, response.getBody());
        verify(loansService, times(1)).getLoansByAccountIds(accountIds);
    }
}
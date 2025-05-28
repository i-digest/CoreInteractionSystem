package com.coreintegration.inbound.controllers;

import com.coreintegration.commons.model.DebitCardDto;
import com.coreintegration.commons.model.DebitCardListResponseDto;
import com.coreintegration.commons.model.DebitCardResponseDto;
import com.coreintegration.inbound.service.DebitCardsService;
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
class DebitCardsControllerTest {

    @Mock
    private DebitCardsService debitCardsService;

    @InjectMocks
    private DebitCardsController debitCardsController;

    private UUID accountId;
    private List<UUID> accountIds;
    private DebitCardResponseDto debitCardResponseDto;
    private DebitCardListResponseDto debitCardListResponseDto;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        accountIds = List.of(accountId, UUID.randomUUID());
        
        // Setup single account response
        debitCardResponseDto = new DebitCardResponseDto();
        debitCardResponseDto.setDebitCards(List.of(new DebitCardDto().id(UUID.randomUUID())));
        
        // Setup multiple accounts response
        debitCardListResponseDto = new DebitCardListResponseDto();
        Map<String, List<DebitCardDto>> debitCardsMap = Map.of(
            accountId.toString(), List.of(new DebitCardDto().id(UUID.randomUUID()))
        );
        debitCardListResponseDto.setDebitCards(debitCardsMap);
    }

    @Test
    void getDebitCardsByAccountId_ShouldReturnDebitCardsForSingleAccount() {
        // Arrange
        when(debitCardsService.getDebitCardsByAccountId(accountId)).thenReturn(debitCardResponseDto);

        // Act
        ResponseEntity<DebitCardResponseDto> response = debitCardsController.getDebitCardsByAccountId(accountId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(debitCardResponseDto, response.getBody());
        verify(debitCardsService, times(1)).getDebitCardsByAccountId(accountId);
    }

    @Test
    void getDebitCardsByAccountIds_ShouldReturnDebitCardsForMultipleAccounts() {
        // Arrange
        when(debitCardsService.getDebitCardsByAccountIds(accountIds)).thenReturn(debitCardListResponseDto);

        // Act
        ResponseEntity<DebitCardListResponseDto> response = debitCardsController.getDebitCardsByAccountIds(accountIds);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(debitCardListResponseDto, response.getBody());
        verify(debitCardsService, times(1)).getDebitCardsByAccountIds(accountIds);
    }
}
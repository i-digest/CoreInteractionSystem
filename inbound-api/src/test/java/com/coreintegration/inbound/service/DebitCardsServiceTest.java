package com.coreintegration.inbound.service;

import com.coreintegration.commons.model.DebitCardDto;
import com.coreintegration.commons.model.DebitCardListResponseDto;
import com.coreintegration.commons.model.DebitCardResponseDto;
import com.coreintegration.database.service.DebitCardsCachedDatabaseServiceAware;
import com.coreintegration.outbound.client.DebitCardsClient;
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
class DebitCardsServiceTest {

    @Mock
    private DebitCardsCachedDatabaseServiceAware cacheServiceAware;

    @Mock
    private DebitCardsClient debitCardsClient;

    @InjectMocks
    private DebitCardsService debitCardsService;


    private UUID accountId;
    private List<UUID> accountIds;
    private List<DebitCardDto> debitCardDtos;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        accountIds = List.of(accountId, UUID.randomUUID());

        // Setup debit card DTOs
        debitCardDtos = List.of(
            new DebitCardDto().id(UUID.randomUUID()),
            new DebitCardDto().id(UUID.randomUUID())
        );
    }

    @Test
    void getDebitCardsByAccountId_ShouldReturnDebitCardsForSingleAccount() {
        // Arrange
        when(cacheServiceAware.getDebitCardsByAccountId(eq(accountId), any(ArrayList.class), any(Supplier.class)))
            .thenReturn(debitCardDtos);

        // Act
        DebitCardResponseDto response = debitCardsService.getDebitCardsByAccountId(accountId);

        // Assert
        assertNotNull(response);
        assertEquals(debitCardDtos, response.getDebitCards());
        verify(cacheServiceAware, times(1)).getDebitCardsByAccountId(eq(accountId), any(ArrayList.class), any(Supplier.class));
    }

    @Test
    void getDebitCardsByAccountIds_ShouldReturnDebitCardsForMultipleAccounts() {
        // Arrange
        when(cacheServiceAware.getDebitCardsByAccountIds(eq(accountIds), any(ArrayList.class), any(Supplier.class)))
            .thenReturn(debitCardDtos);

        // Act
        DebitCardListResponseDto response = debitCardsService.getDebitCardsByAccountIds(accountIds);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getDebitCards());
        // Verify the map contains entries for each debit card
        for (DebitCardDto debitCard : debitCardDtos) {
            String debitCardId = debitCard.getId().toString();
            assertTrue(response.getDebitCards().containsKey(debitCardId));
        }
        verify(cacheServiceAware, times(1)).getDebitCardsByAccountIds(eq(accountIds), any(ArrayList.class), any(Supplier.class));
    }
}

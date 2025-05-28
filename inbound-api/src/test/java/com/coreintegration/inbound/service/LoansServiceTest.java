package com.coreintegration.inbound.service;

import com.coreintegration.commons.model.LoanDto;
import com.coreintegration.commons.model.LoansListResponseDto;
import com.coreintegration.commons.model.LoansResponseDto;
import com.coreintegration.database.service.LoansCachedDatabaseServiceAware;
import com.coreintegration.outbound.client.LoansClient;
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
class LoansServiceTest {

    @Mock
    private LoansCachedDatabaseServiceAware cacheServiceAware;

    @Mock
    private LoansClient loansClient;

    @InjectMocks
    private LoansService loansService;


    private UUID accountId;
    private List<UUID> accountIds;
    private List<LoanDto> loanDtos;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        accountIds = List.of(accountId, UUID.randomUUID());

        // Setup loan DTOs
        loanDtos = List.of(
            new LoanDto().id(UUID.randomUUID()),
            new LoanDto().id(UUID.randomUUID())
        );
    }

    @Test
    void getLoansByAccountId_ShouldReturnLoansForSingleAccount() {
        // Arrange
        when(cacheServiceAware.getLoansByAccountId(eq(accountId), any(ArrayList.class), any(Supplier.class)))
            .thenReturn(loanDtos);

        // Act
        LoansResponseDto response = loansService.getLoansByAccountId(accountId);

        // Assert
        assertNotNull(response);
        assertEquals(loanDtos, response.getLoans());
        verify(cacheServiceAware, times(1)).getLoansByAccountId(eq(accountId), any(ArrayList.class), any(Supplier.class));
    }

    @Test
    void getLoansByAccountIds_ShouldReturnLoansForMultipleAccounts() {
        // Arrange
        when(cacheServiceAware.getLoansByAccountIds(eq(accountIds), any(ArrayList.class), any(Supplier.class)))
            .thenReturn(loanDtos);

        // Act
        LoansListResponseDto response = loansService.getLoansByAccountIds(accountIds);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getLoans());
        // Verify the map contains entries for each loan
        for (LoanDto loan : loanDtos) {
            String loanId = loan.getId().toString();
            assertTrue(response.getLoans().containsKey(loanId));
        }
        verify(cacheServiceAware, times(1)).getLoansByAccountIds(eq(accountIds), any(ArrayList.class), any(Supplier.class));
    }
}

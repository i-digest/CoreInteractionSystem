package com.coreintegration.inbound.controllers;

import com.coreintegration.commons.model.AccountDetailsDto;
import com.coreintegration.commons.model.AccountDetailsListResponseDto;
import com.coreintegration.commons.model.AccountDetailsResponseDto;
import com.coreintegration.inbound.service.AccountDetailsService;
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
class AccountDetailsControllerTest {

    @Mock
    private AccountDetailsService service;

    @InjectMocks
    private AccountDetailsController accountDetailsController;

    private UUID accountId;
    private List<UUID> accountIds;
    private AccountDetailsResponseDto accountDetailsResponseDto;
    private AccountDetailsListResponseDto accountDetailsListResponseDto;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        accountIds = List.of(accountId, UUID.randomUUID());
        
        // Setup single account response
        accountDetailsResponseDto = new AccountDetailsResponseDto();
        accountDetailsResponseDto.setAccountDetails(new AccountDetailsDto().id(accountId));
        
        // Setup multiple accounts response
        accountDetailsListResponseDto = new AccountDetailsListResponseDto();
        Map<String, AccountDetailsDto> accountDetailsMap = Map.of(
            accountId.toString(), new AccountDetailsDto().id(accountId)
        );
        accountDetailsListResponseDto.setAccountDetails(accountDetailsMap);
    }

    @Test
    void getAccountDetailsById_ShouldReturnAccountDetailsForSingleAccount() {
        // Arrange
        when(service.getAccountDetailsById(accountId)).thenReturn(accountDetailsResponseDto);

        // Act
        ResponseEntity<AccountDetailsResponseDto> response = accountDetailsController.getAccountDetailsById(accountId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountDetailsResponseDto, response.getBody());
        verify(service, times(1)).getAccountDetailsById(accountId);
    }

    @Test
    void getAccountsDetailsByIds_ShouldReturnAccountDetailsForMultipleAccounts() {
        // Arrange
        when(service.getAccountsDetailsByIds(accountIds)).thenReturn(accountDetailsListResponseDto);

        // Act
        ResponseEntity<AccountDetailsListResponseDto> response = accountDetailsController.getAccountsDetailsByIds(accountIds);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountDetailsListResponseDto, response.getBody());
        verify(service, times(1)).getAccountsDetailsByIds(accountIds);
    }
}
package com.safebank.safebank_api.service;

import com.safebank.safebank_api.model.Account;
import com.safebank.safebank_api.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount_ShouldSaveAndReturnAccount() {
        // Arrange
        Account inputAccount = new Account();
        inputAccount.setAccountNumber("ACC1234");
        inputAccount.setHolderName("Anthony Stark");
        inputAccount.setBalance(new BigDecimal("500.00"));

        when(accountRepository.save(inputAccount)).thenReturn(inputAccount);

        // Act
        Account savedAccount = accountService.createAccount(inputAccount);

        // Assert
        assertNotNull(savedAccount);
        assertEquals("ACC1234", savedAccount.getAccountNumber());
        verify(accountRepository, times(1)).save(inputAccount);
    }

    @Test
    void shouldReturnAllAccounts_WhenAccountsExist() {
        // Arrange
        Account acc1 = new Account("ACC1234", new BigDecimal("500.00"));
        acc1.setHolderName("Anthony Stark");

        Account acc2 = new Account("ACC5678", new BigDecimal("1000.00"));
        acc2.setHolderName("Pepper Potts");

        List<Account> mockAccounts = List.of(acc1, acc2);
        when(accountRepository.findAll()).thenReturn(mockAccounts);

        // Act
        List<Account> allAccounts = accountService.getAllAccounts();

        // Assert
        assertNotNull(allAccounts);
        assertEquals(2, allAccounts.size());
        assertEquals("ACC1234", allAccounts.get(0).getAccountNumber());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnAccountById_WhenAccountExists() {
        // Arrange
        Account acc2 = new Account("ACC5678", new BigDecimal("1000.00"));
        acc2.setHolderName("Pepper Potts");
        acc2.setId(2L);  // simulate existing ID

        when(accountRepository.findById(2L)).thenReturn(Optional.of(acc2));

        // Act
        Account accountById = accountService.getAccountById(2L);

        // Assert
        assertNotNull(accountById);
        assertEquals("ACC5678", accountById.getAccountNumber());
        assertEquals("Pepper Potts", accountById.getHolderName());
        verify(accountRepository, times(1)).findById(2L);
    }

    @Test
    void shouldThrowException_WhenAccountDoesNotExist() {
        // Arrange
        Long nonExistentId = 99L;
        when(accountRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.getAccountById(nonExistentId);
        });

        assertTrue(exception.getMessage().contains("Account not found"));
        verify(accountRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void shouldUpdateAccount_WhenValidDataProvided() {
        // Arrange
        Long accountId = 1L;

        Account existingAccount = new Account();
        existingAccount.setAccountNumber("ACC1001");
        existingAccount.setHolderName("Tony Stark");
        existingAccount.setBalance(new BigDecimal("500.00"));

        Account updatedData = new Account();
        updatedData.setAccountNumber("ACC1001"); // Account number stays the same
        updatedData.setHolderName("Iron Man");
        updatedData.setBalance(new BigDecimal("1000.00"));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Account result = accountService.updateAccount(accountId, updatedData);

        // Assert
        assertNotNull(result);
        assertEquals("Iron Man", result.getHolderName());
        assertEquals(new BigDecimal("1000.00"), result.getBalance());
        assertEquals("ACC1001", result.getAccountNumber());

        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, times(1)).save(existingAccount);
    }

    @Test
    void shouldThrowException_WhenUpdatingNonexistentAccount() {
        // Arrange
        Long nonExistentId = 99L;
        Account updatedAccount = new Account();
        updatedAccount.setAccountNumber("ACC9999");
        updatedAccount.setHolderName("Updated Name");
        updatedAccount.setBalance(new BigDecimal("999.99"));

        when(accountRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.updateAccount(nonExistentId, updatedAccount);
        });

        assertTrue(exception.getMessage().contains("Account not found"));
        verify(accountRepository, times(1)).findById(nonExistentId);
        verify(accountRepository, never()).save(any());
    }

    @Test
    void shouldDeleteAccount_WhenAccountExists() {
        // Arrange
        Account acc2 = new Account("ACC5678", new BigDecimal("1000.00"));
        acc2.setHolderName("Pepper Potts");
        acc2.setId(2L);  // simulate existing ID

        when(accountRepository.findById(2L)).thenReturn(Optional.of(acc2));

        // Act
        accountService.deleteAccount(2L);

        // Assert
        verify(accountRepository, times(1)).findById(2L);
        verify(accountRepository, times(1)).delete(acc2);
    }

    @Test
    void shouldThrowException_WhenDeletingNonexistentAccount() {
        // Arrange
        Long nonExistentId = 99L;

        when(accountRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.deleteAccount(nonExistentId);
        });

        assertTrue(exception.getMessage().contains("Account not found"));
        verify(accountRepository, times(1)).findById(nonExistentId);
        verify(accountRepository, never()).delete(any());
    }


}

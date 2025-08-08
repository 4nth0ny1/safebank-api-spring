// File: service/AccountService.java
package com.safebank.safebank_api.service;

import com.safebank.safebank_api.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    Account createAccount(Account account);
    List<Account> getAllAccounts();
    Account getAccountById(Long id);
    Account updateAccount(Long id, Account updatedAccount);
    void deleteAccount(Long id);
    Account deposit(Long id, BigDecimal amount);
    Account withdraw(Long id, BigDecimal amount);
}

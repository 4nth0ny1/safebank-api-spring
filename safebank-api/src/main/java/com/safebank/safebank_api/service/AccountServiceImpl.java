// File: service/impl/AccountServiceImpl.java
package com.safebank.safebank_api.service;

import com.safebank.safebank_api.model.Account;
import com.safebank.safebank_api.repository.AccountRepository;
import com.safebank.safebank_api.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + id));
    }

    @Override
    public Account updateAccount(Long id, Account updatedAccount) {
        Account existingAccount = accountRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + id));

        existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
        existingAccount.setBalance(updatedAccount.getBalance());
        existingAccount.setHolderName(updatedAccount.getHolderName());

        return accountRepository.save(existingAccount);
    }

    @Override
    public void deleteAccount(Long id) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + id));

        accountRepository.delete(existingAccount);
    }

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    // Add other methods if needed
}


package com.safebank.safebank_api.controller;

import com.safebank.safebank_api.model.Account;
import com.safebank.safebank_api.repository.AccountRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Account> getAccountById(@PathVariable Long id) {
        return accountRepository.findById(id);
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountRepository.save(account);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id) {
        accountRepository.deleteById(id);
    }
}

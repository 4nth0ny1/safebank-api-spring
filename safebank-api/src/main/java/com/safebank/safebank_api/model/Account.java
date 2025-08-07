package com.safebank.safebank_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Account number is required")
    @Size(min = 5, max = 20, message = "Account number must be between 5 and 20 characters")
    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^ACC\\d{3,17}$", message = "Account number must start with 'ACC' followed by digits")
    private String accountNumber;

    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.00", message = "Balance must be a positive number")
    @DecimalMax(value = "10000000.00", message = "Balance must not exceed 10 million")
    @Digits(integer = 10, fraction = 2, message = "Balance must be a valid monetary amount")
    @Column(nullable = false)
    private BigDecimal balance;

    @NotBlank(message = "Account holder name is required")
    @Size(min = 2, max = 50, message = "Account holder name must be between 2 and 50 characters")
    private String holderName;

    // Constructors
    public Account() {
    }

    public Account(String accountNumber, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}

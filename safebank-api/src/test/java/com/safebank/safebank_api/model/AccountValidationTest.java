package com.safebank.safebank_api.model;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AccountValidationTest {

    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Set<ConstraintViolation<Account>> validate(Account account) {
        return validator.validate(account);
    }

    @Test
    void shouldPassValidation_WhenDataIsValid() {
        Account acc = new Account();
        acc.setAccountNumber("ACC1001");
        acc.setHolderName("Anthony Stark");
        acc.setBalance(new BigDecimal("500.00"));

        Set<ConstraintViolation<Account>> violations = validate(acc);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_WhenAccountNumberIsEmptyOrMissing() {
        Account acc = new Account();
        acc.setHolderName("Anthony Stark");
        acc.setBalance(new BigDecimal("500.00"));

        Set<ConstraintViolation<Account>> violations = validate(acc);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("accountNumber")));
    }

    @Test
    void shouldFailValidation_WhenBalanceIsNegative() {
        Account acc = new Account();
        acc.setAccountNumber("ACC1001");
        acc.setHolderName("Anthony Stark");
        acc.setBalance(new BigDecimal("-500.00"));

        Set<ConstraintViolation<Account>> violations = validate(acc);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("balance")));
    }

    @Test
    void shouldFailValidation_WhenAccountHolderNameIsBlankOrMissing() {
        Account acc = new Account();
        acc.setAccountNumber("ACC1001");
        acc.setBalance(new BigDecimal("500.00"));

        Set<ConstraintViolation<Account>> violations = validate(acc);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("holderName")));
    }

    @Test
    void shouldFailValidation_WhenAccountNumberIsInvalidFormat() {
        Account acc = new Account();
        acc.setAccountNumber("ABC1001");
        acc.setHolderName("Anthony Stark");
        acc.setBalance(new BigDecimal("500.00"));

        Set<ConstraintViolation<Account>> violations = validate(acc);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("accountNumber")));
    }

    @Test
    void shouldFailValidation_WhenBalanceHasTooManyDecimalPlaces() {
        Account acc = new Account();
        acc.setAccountNumber("ACC1001");
        acc.setHolderName("Anthony Stark");
        acc.setBalance(new BigDecimal("500.333"));

        Set<ConstraintViolation<Account>> violations = validate(acc);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("balance")));
    }

    @Test
    void shouldFailValidation_WhenHolderNameExceeds50Chars() {
        Account acc = new Account();
        acc.setAccountNumber("ACC1001");
        acc.setHolderName("Anthony Stark Stark Stark Stark Stark Stark Stark X");
        acc.setBalance(new BigDecimal("500.00"));

        Set<ConstraintViolation<Account>> violations = validate(acc);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("holderName")));
    }

    @Test
    void shouldFailValidation_WhenAccountNumberTooShort() {
        Account acc = new Account();
        acc.setAccountNumber("ACC1");
        acc.setHolderName("Anthony Stark");
        acc.setBalance(new BigDecimal("500.00"));

        Set<ConstraintViolation<Account>> violations = validate(acc);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("accountNumber")));
    }

    @Test
    void shouldFailValidation_WhenBalanceIsNull() {
        Account acc = new Account();
        acc.setAccountNumber("ACC1001");
        acc.setHolderName("Anthony Stark");

        Set<ConstraintViolation<Account>> violations = validate(acc);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("balance")));
    }

    @Test
    void shouldFailValidation_WhenBalanceExceeds10Million() {
        Account acc = new Account();
        acc.setAccountNumber("ACC1001");
        acc.setHolderName("Anthony Stark");
        acc.setBalance(new BigDecimal("500000000.00"));

        Set<ConstraintViolation<Account>> violations = validate(acc);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("balance")));
    }

    @Test
    void shouldFailValidation_WhenHolderNameIsTooShort() {
        Account acc = new Account();
        acc.setAccountNumber("ACC1001");
        acc.setHolderName("A");
        acc.setBalance(new BigDecimal("500.00"));

        Set<ConstraintViolation<Account>> violations = validate(acc);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("holderName")));
    }
}

package com.example.Banking.service;

import com.example.Banking.exception.InsufficientFoundException;
import com.example.Banking.exception.ResourceNotFoundException;
import com.example.Banking.model.Account;
import com.example.Banking.model.Customer;
import com.example.Banking.model.TransactionRecord;
import com.example.Banking.repository.AccountRepository;
import com.example.Banking.repository.CustomerRepository;
import com.example.Banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BankingService {
    private final CustomerRepository customerRepo;
    private final AccountRepository accountRepo;
    private final TransactionRepository txRepo;

    public BankingService(CustomerRepository customerRepo,
                          AccountRepository accountRepo,
                          TransactionRepository txRepo) {
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
        this.txRepo = txRepo;
    }

    public Customer createCustomer(String name, String email) {
        return customerRepo.save(new Customer(name, email));
    }

    public Account createAccount(Long customerId, BigDecimal initialDeposit) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        String acctNumber = generateAccountNumber();
        Account account = new Account(acctNumber, customer, initialDeposit != null ? initialDeposit : BigDecimal.ZERO);
        Account saved = accountRepo.save(account);
        if (initialDeposit != null && initialDeposit.compareTo(BigDecimal.ZERO) > 0) {
            TransactionRecord tr = new TransactionRecord(null, saved, initialDeposit, "DEPOSIT", OffsetDateTime.now());
            txRepo.save(tr);
        }
        return saved;
    }

    public List<Account> listAccounts() {
        return accountRepo.findAll();
    }

    public Account getAccount(Long id) {
        return accountRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    @Transactional
    public Account deposit(String accountNumber, BigDecimal amount) {
        Account account = accountRepo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        account.setBalance(account.getBalance().add(amount));
        Account saved = accountRepo.save(account);
        txRepo.save(new TransactionRecord(null, saved, amount, "DEPOSIT", OffsetDateTime.now()));
        return saved;
    }

    @Transactional
    public Account withdraw(String accountNumber, BigDecimal amount) {
        Account account = accountRepo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFoundException("Not enough balance");
        }
        account.setBalance(account.getBalance().subtract(amount));
        Account saved = accountRepo.save(account);
        txRepo.save(new TransactionRecord(saved, null, amount, "WITHDRAW", OffsetDateTime.now()));
        return saved;
    }

    @Transactional
    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException("Cannot transfer to same account");
        }
        Account from = accountRepo.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("From-account not found"));
        Account to = accountRepo.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("To-account not found"));

        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFoundException("Insufficient funds for transfer");
        }
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        accountRepo.save(from);
        accountRepo.save(to);

        txRepo.save(new TransactionRecord(from, to, amount, "TRANSFER", OffsetDateTime.now()));
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
}

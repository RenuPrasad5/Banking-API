package com.example.Banking.Controller;

import com.example.Banking.dto.AccountRequest;
import com.example.Banking.dto.TransferRequest;
import com.example.Banking.dto.ApiResponse;
import com.example.Banking.model.Account;
import com.example.Banking.model.Customer;
import com.example.Banking.service.BankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final BankingService service;

    public AccountController(BankingService service) {
        this.service = service;
    }

    @PostMapping("/customers")
    public ResponseEntity<ApiResponse> createCustomer(@RequestParam String name, @RequestParam String email) {
        Customer c = service.createCustomer(name, email);
        return ResponseEntity.ok(new ApiResponse("Customer created", c));
    }

    @PostMapping("/accounts")
    public ResponseEntity<ApiResponse> createAccount(@RequestBody AccountRequest request) {
        Account a = service.createAccount(request.getCustomerId(), request.getInitialDeposit());
        return ResponseEntity.ok(new ApiResponse("Account created", a));
    }

    @GetMapping("/accounts")
    public ResponseEntity<ApiResponse> listAccounts() {
        List<Account> list = service.listAccounts();
        return ResponseEntity.ok(new ApiResponse("Accounts list", list));
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<ApiResponse> getAccount(@PathVariable Long id) {
        Account a = service.getAccount(id);
        return ResponseEntity.ok(new ApiResponse("Account fetched", a));
    }

    @PostMapping("/accounts/{accountNumber}/deposit")
    public ResponseEntity<ApiResponse> deposit(@PathVariable String accountNumber, @RequestParam BigDecimal amount) {
        Account a = service.deposit(accountNumber, amount);
        return ResponseEntity.ok(new ApiResponse("Deposited", a));
    }

    @PostMapping("/accounts/{accountNumber}/withdraw")
    public ResponseEntity<ApiResponse> withdraw(@PathVariable String accountNumber, @RequestParam BigDecimal amount) {
        Account a = service.withdraw(accountNumber, amount);
        return ResponseEntity.ok(new ApiResponse("Withdrawn", a));
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse> transfer(@RequestBody TransferRequest request) {
        service.transfer(request.getFromAccount(), request.getToAccount(), request.getAmount());
        return ResponseEntity.ok(new ApiResponse("Transfer completed", null));
    }
}

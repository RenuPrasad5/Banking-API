package com.example.Banking.Controller;

import com.example.Banking.dto.ApiResponse;
import com.example.Banking.model.TransactionRecord;
import com.example.Banking.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> listTransactions() {
        List<TransactionRecord> list = transactionService.listTransaction();
        return ResponseEntity.ok(new ApiResponse("Transactions list", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTransaction(@PathVariable Long id) {
        TransactionRecord t = transactionService.getTransaction(id);
        return ResponseEntity.ok(new ApiResponse("Transaction fetched", t));
    }
}

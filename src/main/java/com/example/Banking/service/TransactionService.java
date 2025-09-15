package com.example.Banking.service;

import com.example.Banking.model.TransactionRecord;
import com.example.Banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // matches: listTransaction()
    public List<TransactionRecord> listTransaction() {
        return transactionRepository.findAll();
    }

    // matches: getTransaction(Long id)
    public TransactionRecord getTransaction(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id " + id));
    }

    // Optional: add create method for later
    public TransactionRecord saveTransaction(TransactionRecord transaction) {
        return transactionRepository.save(transaction);
    }
}

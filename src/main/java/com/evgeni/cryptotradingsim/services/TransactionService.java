package com.evgeni.cryptotradingsim.services;

import com.evgeni.cryptotradingsim.entities.Transaction;
import com.evgeni.cryptotradingsim.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void saveTransaction(Transaction transaction, Connection connection) throws SQLException {
        transactionRepository.insertTransaction(transaction, connection);
    }
}

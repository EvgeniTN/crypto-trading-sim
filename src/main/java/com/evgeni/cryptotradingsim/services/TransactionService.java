package com.evgeni.cryptotradingsim.services;

import com.evgeni.cryptotradingsim.entities.Transaction;
import com.evgeni.cryptotradingsim.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Service class for handling transaction-related business logic in the crypto trading simulation application.
 * It interacts with the TransactionRepository to perform database operations related to transactions.
 */
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**Saves a transaction to the database.*/
    public void saveTransaction(Transaction transaction, Connection connection) throws SQLException {
        transactionRepository.insertTransaction(transaction, connection);
    }
}

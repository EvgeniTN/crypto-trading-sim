package com.evgeni.cryptotradingsim.services;

import com.evgeni.cryptotradingsim.entities.Holding;
import com.evgeni.cryptotradingsim.entities.Transaction;
import com.evgeni.cryptotradingsim.entities.User;
import com.evgeni.cryptotradingsim.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class TradeService {
    private final TransactionService transactionService;
    private final HoldingService holdingService;
    private final UserRepository userRepository;

    @Autowired
    public TradeService(TransactionService transactionService, HoldingService holdingService, UserRepository userRepository) {
        this.transactionService = transactionService;
        this.holdingService = holdingService;
        this.userRepository = userRepository;
    }

    public void executeBuy(Transaction transaction, Holding holding) throws SQLException {
        try (Connection connection = userRepository.getConnection()) {
            try{
                connection.setAutoCommit(false);
                User user = transaction.getUser();
                BigDecimal totalCost = transaction.getPrice().multiply(transaction.getQuantity());
                if (user.getBalance().compareTo(totalCost) < 0) {
                    throw new IllegalArgumentException("Insufficient balance for the transaction.");
                }
                user.setBalance(user.getBalance().subtract(totalCost));
                userRepository.updateUserBalance(user, connection);

                if (transaction.getTimestamp() == null) {
                    transaction.setTimestamp(LocalDateTime.now());
                }

                transactionService.saveTransaction(transaction, connection);
                holdingService.saveOrUpdateHolding(holding, connection);

                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        }
    }
}

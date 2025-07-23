package com.evgeni.cryptotradingsim.services;

import com.evgeni.cryptotradingsim.entities.Holding;
import com.evgeni.cryptotradingsim.entities.Transaction;
import com.evgeni.cryptotradingsim.entities.User;
import com.evgeni.cryptotradingsim.repositories.HoldingRepository;
import com.evgeni.cryptotradingsim.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final HoldingRepository holdingRepository;

    @Autowired
    public TradeService(TransactionService transactionService, HoldingService holdingService, UserRepository userRepository, HoldingRepository holdingRepository) {
        this.transactionService = transactionService;
        this.holdingService = holdingService;
        this.userRepository = userRepository;
        this.holdingRepository = holdingRepository;
    }

    public void executeBuy(Transaction transaction, Holding holding) throws SQLException {
        try (Connection connection = userRepository.getConnection()) {
            try{
                connection.setAutoCommit(false);
                User user = transaction.getUser();
                BigDecimal totalCost = transaction.getPrice().multiply(transaction.getQuantity());
                BigDecimal roundedBalance = user.getBalance().setScale(6, RoundingMode.HALF_UP);
                BigDecimal epsilon = new BigDecimal("0.000001");
                if (roundedBalance.add(epsilon).compareTo(totalCost) < 0){
                    throw new IllegalArgumentException("Insufficient balance for the transaction.");
                };
                user.setBalance(user.getBalance().subtract(totalCost).setScale(6, RoundingMode.HALF_UP));
                userRepository.updateUserBalance(user, connection);

                if (transaction.getTimestamp() == null) {
                    transaction.setTimestamp(LocalDateTime.now());
                }

                Holding currentHolding = holdingService.getHoldingByUserAndSymbol(user, transaction.getSymbol(), connection);
                BigDecimal currentQuantity = currentHolding != null && currentHolding.getQuantity() != null
                        ? currentHolding.getQuantity() : BigDecimal.ZERO;
                BigDecimal currentAveragePrice = currentHolding != null && currentHolding.getAveragePrice() != null
                        ? currentHolding.getAveragePrice() : BigDecimal.ZERO;

                BigDecimal newQuantity = currentQuantity.add(transaction.getQuantity());
                BigDecimal newAveragePrice = (currentQuantity.multiply(currentAveragePrice)
                        .add(transaction.getQuantity().multiply(transaction.getPrice())))
                        .divide(newQuantity, 6, RoundingMode.HALF_UP);

                holding.setQuantity(newQuantity);
                holding.setAveragePrice(newAveragePrice);

                transactionService.saveTransaction(transaction, connection);
                holdingService.saveOrUpdateHolding(holding, connection);

                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        }
    }

    public void executeSell(Transaction transaction, Holding holding) throws SQLException {
        try (Connection connection = userRepository.getConnection()) {
            try{
                connection.setAutoCommit(false);
                User user = transaction.getUser();
                BigDecimal totalCost = transaction.getPrice().multiply(transaction.getQuantity());
                BigDecimal epsilon = new BigDecimal("0.000001");

                BigDecimal actualHoldings = holdingRepository.retrieveHoldingsByUserId(user).getOrDefault(transaction.getSymbol(), BigDecimal.ZERO);
                BigDecimal newHoldings = actualHoldings.subtract(transaction.getQuantity());

                if (newHoldings.add(epsilon).compareTo(BigDecimal.ZERO) < 0 &&
                        newHoldings.abs().compareTo(epsilon) > 0){
                    throw new IllegalArgumentException("Insufficient holdings for the transaction.");
                }

                if (newHoldings.compareTo(BigDecimal.ZERO) < 0 && newHoldings.abs().compareTo(epsilon) <= 0) {
                    newHoldings = BigDecimal.ZERO;
                }

                user.setBalance(user.getBalance().add(totalCost).setScale(6, RoundingMode.HALF_UP));
                holding.setQuantity(newHoldings.setScale(6, RoundingMode.HALF_UP));
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

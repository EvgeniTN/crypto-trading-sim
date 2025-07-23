package com.evgeni.cryptotradingsim.services;

import com.evgeni.cryptotradingsim.entities.Holding;
import com.evgeni.cryptotradingsim.entities.Transaction;
import com.evgeni.cryptotradingsim.entities.User;
import com.evgeni.cryptotradingsim.repositories.HoldingRepository;
import com.evgeni.cryptotradingsim.repositories.TransactionRepository;
import com.evgeni.cryptotradingsim.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Service class for user-related business logic in the crypto trading simulation application.
 * Handles user registration and delegates database operations to the UserRepository.
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final HoldingRepository holdingRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public UserService(UserRepository userRepository, HoldingRepository holdingRepository,
                       TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.holdingRepository = holdingRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
    * Registers a new user by setting an initial balance and saving the user to the database.
    */
    public void registerUser(User user) throws SQLException {
        user.setBalance(new BigDecimal("10000.00"));
        userRepository.addUser(user);
    }

    /**
     * Authenticates a user by checking their username and password.
     * @return User object if authentication is successful, otherwise null.
     */
    public User login(String email, String password) throws SQLException {
        return userRepository.findByUsernameAndPassword(email, password);
    }

    /**Retrieves all holdings for a given user.*/
    public Map<String, BigDecimal> getHoldingsByUserId(User user) throws SQLException {
        return holdingRepository.retrieveHoldingsByUserId(user);
    }

    /**Retrieves a user by their ID.*/
    public User getUserById(int id) throws SQLException {
        return userRepository.findById(id);
    }

    /**Retrieves all transactions for a given user.*/
    public List<Transaction> getTransactionsByUserId(User user) throws SQLException {
        return transactionRepository.retrieveTransactionByUserId(user);
    }

    /**Retrieves the avereage price for each holding of a user by their ID and symbol.*/
    public BigDecimal getAveragePriceByUserIdAndSymbol(User user, String symbol) throws SQLException {
        return holdingRepository.retrieveAveragePriceByUserIdAndSymbol(user, symbol);
    }

    /**Updates a user's information in the database.*/
    public User resetAccount(Long user_id) throws SQLException {
        User user = userRepository.findById(user_id.intValue());
        user.setBalance(new BigDecimal("10000.00"));
        holdingRepository.deleteHoldingsByUserId(user);
        transactionRepository.deleteHistoryByUserId(user);
        userRepository.updateUser(user);
        return user;
    }
}

package com.evgeni.cryptotradingsim.services;

import com.evgeni.cryptotradingsim.entities.User;
import com.evgeni.cryptotradingsim.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Service class for user-related business logic in the crypto trading simulation application.
 * Handles user registration and delegates database operations to the UserRepository.
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
    * Registers a new user by setting an initial balance and saving the user to the database.
    */
    public void registerUser(User user) throws SQLException {
        user.setBalance(new BigDecimal("10000.00"));
        userRepository.addUser(user);
    }
}

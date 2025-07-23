package com.evgeni.cryptotradingsim.controllers;

import com.evgeni.cryptotradingsim.entities.Holding;
import com.evgeni.cryptotradingsim.entities.Transaction;
import com.evgeni.cryptotradingsim.entities.User;
import com.evgeni.cryptotradingsim.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * REST controller for user-related operations.
 */
@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**Endpoint for testing controller is working*/
    @GetMapping("/test")
    public String test() {
        return "UserController is working";
    }

    /**Registers a new user*/
    @PostMapping("register")
    public void registerUser(@RequestBody User user) throws SQLException {
        userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) throws SQLException {
        User found = userService.login(user.getUsername(), user.getPassword());
        if (found != null) {
            return ResponseEntity.ok(found);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/holdings")
    public ResponseEntity<Map<String, BigDecimal>> getHoldings(@RequestBody User user) throws SQLException {
        Map<String, BigDecimal> holdings = userService.getHoldingsByUserId(user);
        return ResponseEntity.ok(holdings);
    }

    @PostMapping("/average-price")
    public ResponseEntity<BigDecimal> getAveragePrice(@RequestBody Map<String, Object> payload) throws SQLException {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.convertValue(payload.get("user"), User.class);
        String symbol = (String) payload.get("symbol");
        BigDecimal avgPrice = userService.getAveragePriceByUserIdAndSymbol(user, symbol);
        return ResponseEntity.ok(avgPrice);
    }

    @PostMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@RequestBody User user) throws SQLException {
        List<Transaction> transactions = userService.getTransactionsByUserId(user);
        if (transactions != null && !transactions.isEmpty()) {
            return ResponseEntity.ok(transactions);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) throws SQLException {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}

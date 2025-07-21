package com.evgeni.cryptotradingsim.controllers;

import com.evgeni.cryptotradingsim.entities.User;
import com.evgeni.cryptotradingsim.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

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
}

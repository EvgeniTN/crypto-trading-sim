package com.evgeni.cryptotradingsim.entities;

import lombok.Data;

import java.math.BigDecimal;

/**Represents a user entity in the application*/
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private BigDecimal balance;
}

package com.evgeni.cryptotradingsim.entities;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Holding {
    private Long id;
    private String symbol;
    private BigDecimal quantity;
    private User user;
}

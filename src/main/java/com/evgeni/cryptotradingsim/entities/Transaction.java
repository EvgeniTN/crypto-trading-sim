package com.evgeni.cryptotradingsim.entities;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Represents a transaction entity in the application.*/
@Data
public class Transaction {
    private Long id;
    private Boolean buy;
    private BigDecimal price;
    private BigDecimal quantity;
    private BigDecimal total;
    private LocalDateTime timestamp;
    private User user;
    private String symbol;
}

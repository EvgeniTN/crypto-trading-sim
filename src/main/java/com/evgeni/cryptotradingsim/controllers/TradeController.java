package com.evgeni.cryptotradingsim.controllers;

import com.evgeni.cryptotradingsim.entities.Holding;
import com.evgeni.cryptotradingsim.entities.Transaction;
import com.evgeni.cryptotradingsim.services.TradeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/api/trade")
public class TradeController {
    private final TradeService tradeService;

    @Autowired
    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @PostMapping("/buy")
    public void executeBuy(@RequestBody Map<String, Object> payload) throws SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        Transaction transaction = objectMapper.convertValue(payload.get("transaction"), Transaction.class);
        Holding holding = objectMapper.convertValue(payload.get("holding"), Holding.class);
        tradeService.executeBuy(transaction, holding);
    }
}

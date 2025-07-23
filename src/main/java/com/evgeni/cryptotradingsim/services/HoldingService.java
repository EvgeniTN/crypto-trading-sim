package com.evgeni.cryptotradingsim.services;

import com.evgeni.cryptotradingsim.entities.Holding;
import com.evgeni.cryptotradingsim.entities.User;
import com.evgeni.cryptotradingsim.repositories.HoldingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class HoldingService {
    private final HoldingRepository holdingRepository;

    public HoldingService(HoldingRepository holdingRepository) {
        this.holdingRepository = holdingRepository;
    }

    public void saveOrUpdateHolding(Holding holding, Connection connection) throws SQLException {
        holdingRepository.saveOrUpdateHolding(holding, connection);
    }

    public Holding getHoldingByUserAndSymbol(User user, String symbol, Connection connection) throws SQLException {
        return holdingRepository.findHoldingByUserAndSymbol(user, symbol, connection);
    }

    public BigDecimal getAveragePriceByUserIdAndSymbol(User user, String symbol) throws SQLException {
        return holdingRepository.retrieveAveragePriceByUserIdAndSymbol(user, symbol);
    }
}

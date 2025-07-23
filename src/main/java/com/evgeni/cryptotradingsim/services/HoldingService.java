package com.evgeni.cryptotradingsim.services;

import com.evgeni.cryptotradingsim.entities.Holding;
import com.evgeni.cryptotradingsim.entities.User;
import com.evgeni.cryptotradingsim.repositories.HoldingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Service class for managing holdings in the crypto trading simulation application.
 * It provides methods to save or update holdings, retrieve holdings by user and symbol,
 * and retrieve the average price of holdings.
 */
@Service
public class HoldingService {
    private final HoldingRepository holdingRepository;

    public HoldingService(HoldingRepository holdingRepository) {
        this.holdingRepository = holdingRepository;
    }

    /**Saves or updates a holding in the database.*/
    public void saveOrUpdateHolding(Holding holding, Connection connection) throws SQLException {
        holdingRepository.saveOrUpdateHolding(holding, connection);
    }

    /**Retrieves a holding by user and symbol from the database.*/
    public Holding getHoldingByUserAndSymbol(User user, String symbol, Connection connection) throws SQLException {
        return holdingRepository.findHoldingByUserAndSymbol(user, symbol, connection);
    }

    /**Retrieves the average price of a holding by user and symbol from the database.*/
    public BigDecimal getAveragePriceByUserIdAndSymbol(User user, String symbol) throws SQLException {
        return holdingRepository.retrieveAveragePriceByUserIdAndSymbol(user, symbol);
    }
}

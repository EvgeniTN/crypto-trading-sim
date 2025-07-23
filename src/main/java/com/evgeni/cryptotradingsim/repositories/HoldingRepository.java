package com.evgeni.cryptotradingsim.repositories;

import com.evgeni.cryptotradingsim.entities.Holding;
import com.evgeni.cryptotradingsim.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Repository
public class HoldingRepository {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void saveOrUpdateHolding(Holding holding, Connection connection) throws SQLException {
        String selectSql = "SELECT id FROM holdings WHERE user_id = ? AND symbol = ?";
        try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
            selectStatement.setString(1, holding.getUser().getId().toString());
            selectStatement.setString(2, holding.getSymbol());
            var resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                String updateSql = "UPDATE holdings SET quantity = ?, average_price = ? WHERE id = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                    updateStatement.setBigDecimal(1, holding.getQuantity());
                    updateStatement.setBigDecimal(2, holding.getAveragePrice());
                    updateStatement.setLong(3, resultSet.getLong("id"));
                    updateStatement.executeUpdate();
                }
            } else {
                String insertSql = "INSERT INTO holdings (symbol, quantity, user_id, average_price) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                    insertStatement.setString(1, holding.getSymbol());
                    insertStatement.setBigDecimal(2, holding.getQuantity());
                    insertStatement.setLong(3, holding.getUser().getId());
                    insertStatement.setBigDecimal(4, holding.getAveragePrice());
                    insertStatement.executeUpdate();
                }
            }
        }
    }

    public Map<String, BigDecimal> retrieveHoldingsByUserId(User user) throws SQLException {
        String sql = "SELECT symbol, quantity FROM holdings WHERE user_id = ?";
        Map<String, BigDecimal> holdings = new HashMap<>();
        try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1, user.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    holdings.put(resultSet.getString("symbol"),
                            resultSet.getBigDecimal("quantity"));
                }
            }
        }
        return holdings;
    }

    public Holding findHoldingByUserAndSymbol(User user, String symbol, Connection connection) throws SQLException {
        String sql = "SELECT id, symbol, quantity, average_price FROM holdings WHERE user_id = ? AND symbol = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            statement.setString(2, symbol);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Holding holding = new Holding();
                    holding.setId(resultSet.getLong("id"));
                    holding.setSymbol(resultSet.getString("symbol"));
                    holding.setQuantity(resultSet.getBigDecimal("quantity"));
                    holding.setAveragePrice(resultSet.getBigDecimal("average_price"));
                    holding.setUser(user);
                    return holding;
                }
            }
        }
        return null; // Return null if no holding is found
    }

    public BigDecimal retrieveAveragePriceByUserIdAndSymbol(User user, String symbol) throws SQLException {
        String sql = "SELECT average_price FROM holdings WHERE user_id = ? AND symbol = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            statement.setString(2, symbol);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBigDecimal("average_price");
                }
            }
        }
        return null;
    }

}

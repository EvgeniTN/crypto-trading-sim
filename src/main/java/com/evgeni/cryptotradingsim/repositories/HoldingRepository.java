package com.evgeni.cryptotradingsim.repositories;

import com.evgeni.cryptotradingsim.entities.Holding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class HoldingRepository {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.user}")
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
                String updateSql = "UPDATE holdings SET quantity = ? WHERE id = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                    updateStatement.setBigDecimal(1, holding.getQuantity());
                    updateStatement.setLong(2, resultSet.getLong("id"));
                    updateStatement.executeUpdate();
                }
            } else {
                String insertSql = "INSERT INTO holdings (symbol, quantity, user_id) VALUES (?, ?, ?)";
                try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                    insertStatement.setString(1, holding.getSymbol());
                    insertStatement.setBigDecimal(2, holding.getQuantity());
                    insertStatement.setLong(3, holding.getUser().getId());
                    insertStatement.executeUpdate();
                }
            }
        }
    }
}

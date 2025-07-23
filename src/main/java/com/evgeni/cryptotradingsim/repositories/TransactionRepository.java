package com.evgeni.cryptotradingsim.repositories;

import com.evgeni.cryptotradingsim.entities.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Repository
public class TransactionRepository {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.user}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void insertTransaction(Transaction transaction, Connection connection) throws SQLException {
        String sqlQuery = "INSERT INTO transactions (buy, price, quantity, total, timestamp,"+
                " user_id, symbol) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (var preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setBoolean(1, transaction.getBuy());
            preparedStatement.setBigDecimal(2, transaction.getPrice());
            preparedStatement.setBigDecimal(3, transaction.getQuantity());
            preparedStatement.setBigDecimal(4, transaction.getTotal());
            preparedStatement.setObject(5, transaction.getTimestamp());
            preparedStatement.setLong(6, transaction.getUser().getId());
            preparedStatement.setString(7, transaction.getSymbol());
            preparedStatement.executeUpdate();
        }
    }
}

package com.evgeni.cryptotradingsim.repositories;

import com.evgeni.cryptotradingsim.entities.Transaction;
import com.evgeni.cryptotradingsim.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for managing transaction entities in the database.
 * Provides methods for database operations related to transactions.
 */
@Repository
public class TransactionRepository {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**Inserts a new transaction into the 'transactions' table in the database.*/
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

    /**Retrieves a list of transactions for a specific user.*/
    public List<Transaction> retrieveTransactionByUserId(User user) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE user_id = ?";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, user.getId());
           try (var rs = preparedStatement.executeQuery()) {
               while (rs.next()) {
                     Transaction transaction = new Transaction();
                     transaction.setId(rs.getLong("id"));
                     transaction.setBuy(rs.getBoolean("buy"));
                     transaction.setPrice(rs.getBigDecimal("price"));
                     transaction.setQuantity(rs.getBigDecimal("quantity"));
                     transaction.setTotal(rs.getBigDecimal("total"));
                     transaction.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                     transaction.setSymbol(rs.getString("symbol"));

                     transactions.add(transaction);
               }
           }
        }
        return transactions;
    }
}

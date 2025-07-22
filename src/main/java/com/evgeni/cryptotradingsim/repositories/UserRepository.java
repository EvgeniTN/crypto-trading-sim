package com.evgeni.cryptotradingsim.repositories;

import com.evgeni.cryptotradingsim.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Repository class for managing user entities in the database.
 * Provides methods for database operations related to users.
 */
@Repository
public class UserRepository {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.user}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Inserts a new user into the 'users' table in the database
     * */
    public void addUser(User user) throws SQLException {

        String sqlQuery = "INSERT INTO users (username, password, balance) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setBigDecimal(3, user.getBalance());
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Finds a user by username and password.
     * @return User object if found, otherwise null
     */
    public User findByUsernameAndPassword(String username, String password) throws SQLException {
        String sqlQuery = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setBalance(resultSet.getBigDecimal("balance"));
                return user;
            }
            return null;
        }
    }

    public void updateUserBalance(User user, Connection connection) throws SQLException {
        String sql = "UPDATE users SET balance = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, user.getBalance());
            stmt.setLong(2, user.getId());
            stmt.executeUpdate();
        }
    }
}

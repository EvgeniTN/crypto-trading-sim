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
}

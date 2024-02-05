package com.dmdev.integration;

import com.dmdev.util.ConnectionManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

public abstract class IntegrationTestBase {

    private static final String CLEAN_SQL = "DELETE FROM subscription;";
    private static final String CREATE_SQL = """
            CREATE TABLE IF NOT EXISTS subscription
            (
                id INT AUTO_INCREMENT PRIMARY KEY ,
                user_id INT NOT NULL ,
                name VARCHAR(64) NOT NULL ,
                provider VARCHAR(16) NOT NULL ,
                expiration_date DATETIME NOT NULL ,
                status VARCHAR(16) NOT NULL ,
                UNIQUE (user_id, name)
            );
            """;

    @BeforeAll
    static void prepareDatabase() throws SQLException {
        try (var connection = ConnectionManager.get();
             var statement = connection.createStatement()) {
            statement.execute(CREATE_SQL);
        }
    }

    @BeforeEach
    void cleanData() throws SQLException {
        try (var connection = ConnectionManager.get();
             var statement = connection.createStatement()) {
            statement.execute(CLEAN_SQL);
        }
    }
}

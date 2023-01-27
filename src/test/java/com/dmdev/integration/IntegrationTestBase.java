package com.dmdev.integration;

import com.dmdev.util.ConnectionManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

public abstract class IntegrationTestBase {

    private static final String CLEAN_SQL = "DELETE FROM users;";
    private static final String CREATE_SQL = """
            CREATE TABLE IF NOT EXISTS users
            (
                id INT AUTO_INCREMENT PRIMARY KEY ,
                name VARCHAR(64) NOT NULL,
                birthday DATE NOT NULL ,
                email VARCHAR(64) NOT NULL UNIQUE ,
                password VARCHAR(64) NOT NULL ,
                role VARCHAR(32) NOT NULL ,
                gender VARCHAR(16) NOT NULL
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

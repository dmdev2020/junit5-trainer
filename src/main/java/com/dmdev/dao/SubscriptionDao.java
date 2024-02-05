package com.dmdev.dao;

import com.dmdev.entity.Provider;
import com.dmdev.entity.Status;
import com.dmdev.entity.Subscription;
import com.dmdev.util.ConnectionManager;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SubscriptionDao implements Dao<Integer, Subscription> {

    private static final SubscriptionDao INSTANCE = new SubscriptionDao();

    private static final String GET_ALL_SQL = """
            SELECT
                id,
                user_id,
                name,
                provider,
                expiration_date,
                status
            FROM subscription
            """;
    private static final String GET_BY_ID_SQL = GET_ALL_SQL + " WHERE id = ?";
    private static final String GET_BY_USER_ID_SQL = GET_ALL_SQL + " WHERE user_id = ?";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM subscription WHERE id = ?";
    private static final String SAVE_SQL =
            "INSERT INTO subscription (user_id, name, provider, expiration_date, status) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_BY_ID_SQL = """
            UPDATE subscription
            SET user_id = ?,
                name = ?,
                provider = ?,
                expiration_date = ?,
                status = ?
            WHERE id = ?
            """;

    public static SubscriptionDao getInstance() {
        return INSTANCE;
    }

    @Override
    @SneakyThrows
    public List<Subscription> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(GET_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Subscription> subscriptions = new ArrayList<>();
            while (resultSet.next()) {
                subscriptions.add(buildEntity(resultSet));
            }

            return subscriptions;
        }
    }

    @Override
    @SneakyThrows
    public Optional<Subscription> findById(Integer id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(GET_BY_ID_SQL)) {
            preparedStatement.setObject(1, id);

            var resultSet = preparedStatement.executeQuery();
            return resultSet.next()
                    ? Optional.of(buildEntity(resultSet))
                    : Optional.empty();
        }
    }

    @Override
    @SneakyThrows
    public boolean delete(Integer id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            preparedStatement.setObject(1, id);

            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    @SneakyThrows
    public Subscription update(Subscription entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_BY_ID_SQL)) {
            prepareStatementToUpsert(preparedStatement, entity);
            preparedStatement.setObject(6, entity.getId());

            preparedStatement.executeUpdate();
            return entity;
        }
    }

    @Override
    @SneakyThrows
    public Subscription insert(Subscription entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, RETURN_GENERATED_KEYS)) {
            prepareStatementToUpsert(preparedStatement, entity);

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            entity.setId(generatedKeys.getObject("id", Integer.class));

            return entity;
        }
    }

    @SneakyThrows
    public List<Subscription> findByUserId(Integer userId) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(GET_BY_USER_ID_SQL)) {
            preparedStatement.setObject(1, userId);

            var resultSet = preparedStatement.executeQuery();
            List<Subscription> subscriptions = new ArrayList<>();
            while (resultSet.next()) {
                subscriptions.add(buildEntity(resultSet));
            }

            return subscriptions;
        }
    }

    private Subscription buildEntity(ResultSet resultSet) throws SQLException {
        return Subscription.builder()
                .id(resultSet.getObject("id", Integer.class))
                .userId(resultSet.getObject("user_id", Integer.class))
                .name(resultSet.getObject("name", String.class))
                .provider(Provider.valueOf(resultSet.getObject("provider", String.class)))
                .expirationDate(resultSet.getObject("expiration_date", Timestamp.class).toInstant())
                .status(Status.valueOf(resultSet.getObject("status", String.class)))
                .build();
    }

    private void prepareStatementToUpsert(PreparedStatement preparedStatement, Subscription entity) throws SQLException {
        preparedStatement.setObject(1, entity.getUserId());
        preparedStatement.setObject(2, entity.getName());
        preparedStatement.setObject(3, entity.getProvider().name());
        preparedStatement.setObject(4, Timestamp.from(entity.getExpirationDate()));
        preparedStatement.setObject(5, entity.getStatus().name());
    }
}

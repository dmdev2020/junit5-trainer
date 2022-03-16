package com.dmdev.dao;

import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.util.ConnectionManager;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UserDao implements Dao<Integer, User> {

    private static final UserDao INSTANCE = new UserDao();

    private static final String GET_ALL_SQL = """
            SELECT
                id,
                name,
                birthday,
                email,
                password,
                role,
                gender
            FROM users
            """;
    private static final String GET_BY_ID_SQL = GET_ALL_SQL + " WHERE id = ?";
    private static final String GET_BY_EMAIL_AND_PASSWORD_SQL = GET_ALL_SQL + " WHERE email = ? AND password = ?";
    private static final String SAVE_SQL =
            "INSERT INTO users (name, birthday, email, password, role, gender) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM users WHERE id = ?";
    private static final String UPDATE_BY_ID_SQL = """
            UPDATE users
            SET name = ?,
                birthday = ?,
                email = ?,
                password = ?,
                role = ?,
                gender = ?
            WHERE id = ?
            """;

    public static UserDao getInstance() {
        return INSTANCE;
    }

    @Override
    @SneakyThrows
    public List<User> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(GET_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(buildEntity(resultSet));
            }

            return users;
        }
    }

    @Override
    @SneakyThrows
    public Optional<User> findById(Integer id) {
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
    public User save(User entity) {
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
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(GET_BY_EMAIL_AND_PASSWORD_SQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

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
    public void update(User entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_BY_ID_SQL)) {
            prepareStatementToUpsert(preparedStatement, entity);
            preparedStatement.setObject(7, entity.getId());

            preparedStatement.executeUpdate();
        }
    }

    private User buildEntity(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getObject("id", Integer.class))
                .name(resultSet.getObject("name", String.class))
                .birthday(resultSet.getObject("birthday", Date.class).toLocalDate())
                .email(resultSet.getObject("email", String.class))
                .password(resultSet.getObject("password", String.class))
                .role(Role.find(resultSet.getObject("role", String.class)).orElse(null))
                .gender(Gender.find(resultSet.getObject("gender", String.class)).orElse(null))
                .build();
    }

    private void prepareStatementToUpsert(PreparedStatement preparedStatement, User entity) throws SQLException {
        preparedStatement.setObject(1, entity.getName());
        preparedStatement.setObject(2, entity.getBirthday());
        preparedStatement.setObject(3, entity.getEmail());
        preparedStatement.setObject(4, entity.getPassword());
        preparedStatement.setObject(5, entity.getRole() != null ? entity.getRole().name() : null);
        preparedStatement.setObject(6, entity.getGender() != null ? entity.getGender().name() : null);
    }
}

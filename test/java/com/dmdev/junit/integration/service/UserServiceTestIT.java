package com.dmdev.junit.integration.service;

import com.dmdev.exception.ValidationException;
import com.dmdev.integration.IntegrationTestBase;
import com.dmdev.junit.utility.UsersCreator;
import com.dmdev.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.assertThat;


class UserServiceTestIT extends IntegrationTestBase {

    private final UserService userService = UserService.getInstance();

    @ParameterizedTest
    @CsvFileSource(resources = "/testusersnegative.csv", delimiter = ',', numLinesToSkip = 1)
    void loginNegativeParametrized(String email, String password) {
        var result = userService.login(email, password);

        assertThat(result).isEmpty();
    }

    @Test
    void loginPositive() {
        var result = userService.login("vlad@gmail.com", "456");

        assertThat(result).isNotEmpty();
    }

    @Test
    void userCreationPositive() {
        var user = UsersCreator.getCreateUserValid();
        var result = userService.create(user);

        assertThat(result.getEmail().equals(user.getEmail())).isTrue();
    }

    @Test
    void userCreationPositiveAllErrors() {
        try {
            UsersCreator.getCreateUserInvalidAll();
        } catch (ValidationException exception) {
            assertThat(exception.getErrors().size()).isEqualTo(3);
        }
    }
}

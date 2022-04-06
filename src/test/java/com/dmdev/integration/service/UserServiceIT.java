package com.dmdev.integration.service;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.exception.ValidationException;
import com.dmdev.generator.UserGenerator;
import com.dmdev.integration.IntegrationTestBase;
import com.dmdev.service.UserService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserServiceIT extends IntegrationTestBase {

    private final UserService userService = UserService.getInstance();
    private CreateUserDto userDto;

    @Test
    void loginSuccessIfUserExist() {
        var existUser = userService.login("petr@gmail.com", "123");
        assertThat(existUser).isPresent();
    }

    @Test
    void failedToCreateUserBecauseNotValidateDate() {
        try {
            userDto = UserGenerator.getNotCorrectUser();
            userService.create(userDto);
        } catch (ValidationException exception) {
            assertThat(exception.getErrors().size()).isEqualTo(3);
        }
    }

    @Test
    void userCreationSuccessful() {
        userDto = UserGenerator.getCorrectUser();
        var existUser = userService.create(this.userDto);
        assertThat(existUser.getId()).isEqualTo(6);
    }
}

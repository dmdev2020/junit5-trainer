package com.dmdev.junit.validator;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.junit.utility.UsersCreator;
import com.dmdev.validator.CreateUserValidator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateUserValidatorTest {

    private final CreateUserValidator createUserValidator = CreateUserValidator.getInstance();

    @Test
    void createUserValidatorVerifyBirthday() {
        CreateUserDto createUserDto = UsersCreator.getCreateUserInvalidBirthday();
        var actualResult = createUserValidator.validate(createUserDto);

        assertThat(actualResult.getErrors()).hasSize(1);
    }

    @Test
    void createUserValidatorVerifyGender() {
        CreateUserDto createUserDto = UsersCreator.getCreateUserInvalidGender();
        var actualResult = createUserValidator.validate(createUserDto);

        assertThat(actualResult.getErrors()).hasSize(1);
    }

    @Test
    void createUserValidatorVerifyRole() {
        CreateUserDto createUserDto = UsersCreator.getCreateUserInvalidRole();
        var actualResult = createUserValidator.validate(createUserDto);

        assertThat(actualResult.getErrors()).hasSize(1);
    }

    @Test
    void createUserValidatorVerifyPositive() {
        CreateUserDto createUserDto = UsersCreator.getCreateUserValid();
        var actualResult = createUserValidator.validate(createUserDto);

        assertThat(actualResult.getErrors()).hasSize(0);
    }
}

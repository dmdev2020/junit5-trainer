package com.dmdev.validator;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.generator.UserGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateUserValidatorTest {

    private final CreateUserValidator createUserValidator = CreateUserValidator.getInstance();


    @ParameterizedTest
    @MethodSource("getErrorValidate")
    void checkEveryValidateError(String birthday, String gender, String role) {

        CreateUserDto dto = CreateUserDto.builder()
                .birthday(birthday)
                .gender(gender)
                .role(role)
                .build();

        var actualResult = createUserValidator.validate(dto);

        assertFalse(actualResult.isValid());
        assertThat(actualResult.getErrors()).hasSize(1);
    }

    @Test
    void checkValidUser() {

        var dto = UserGenerator.getCreateUserDto();

        var actualResult = createUserValidator.validate(dto);

        assertTrue(actualResult.isValid());
        assertThat(actualResult.getErrors()).hasSize(0);
    }

    @Test
    void checkAllValidateError() {
        var dto = UserGenerator.getNotCorrectUser();

        var actualResult = createUserValidator.validate(dto);

        assertFalse(actualResult.isValid());
        assertThat(actualResult.getErrors()).hasSize(3);
    }

    static Stream<Arguments> getErrorValidate() {
        return Stream.of(
                Arguments.of("dummy", "MALE", "USER"),
                Arguments.of("1989-08-09", "dummy", "USER"),
                Arguments.of("1989-08-09", "MALE", "dummy")
        );
    }
}

package com.dmdev.validator;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateUserValidatorTest {

    private final CreateUserValidator validator = CreateUserValidator.getInstance();

    /*
     * Отличный вариант использования параметризованных тестов, когда у нас есть множество различных ошибок валидации
     * при передаче различных параметров в тестируемый метод
     */
    @MethodSource("getValidationErrorArguments")
    @ParameterizedTest
    void checkEachValidationError(String birthday, String role, String gender, String expectedErrorCode) {
        CreateUserDto dto = CreateUserDto.builder()
                .birthday(birthday)
                .role(role)
                .gender(gender)
                .build();

        ValidationResult validationResult = validator.validate(dto);

        assertFalse(validationResult.isValid());
        assertThat(validationResult.getErrors()).hasSize(1);
        assertThat(validationResult.getErrors().get(0).getCode()).isEqualTo(expectedErrorCode);
    }

    @Test
    void shouldReturnValidResultIfObjectIsValid() {
        CreateUserDto dto = CreateUserDto.builder()
                .birthday("2000-09-25")
                .role(Role.ADMIN.name())
                .gender(Gender.FEMALE.name())
                .build();

        ValidationResult validationResult = validator.validate(dto);

        assertTrue(validationResult.isValid());
        assertThat(validationResult.getErrors()).isEmpty();
    }

    @Test
    void shouldCombineAllValidationErrors() {
        CreateUserDto dto = CreateUserDto.builder()
                .birthday("09-25-2000")
                .role("dummy")
                .gender("dummy")
                .build();

        ValidationResult validationResult = validator.validate(dto);

        assertFalse(validationResult.isValid());
        assertThat(validationResult.getErrors()).hasSize(3);
        assertThat(validationResult.getErrors().stream().map(Error::getCode))
                .contains("invalid.birthday", "invalid.gender", "invalid.role");
    }

    /**
     * @return arguments: Birthday, Role, Gender, Expected Error code
     */
    private static Stream<Arguments> getValidationErrorArguments() {
        return Stream.of(
            Arguments.of("09-25-2000", Role.ADMIN.name(), Gender.FEMALE.name(), "invalid.birthday"),
            Arguments.of("2000-09-25", Role.ADMIN.name(), "dummy", "invalid.gender"),
            Arguments.of("2000-09-25", "dummy", Gender.MALE.name(), "invalid.role")
        );
    }
}
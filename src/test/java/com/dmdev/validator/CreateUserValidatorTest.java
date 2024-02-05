package com.dmdev.validator;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateUserValidatorTest {

    private final CreateUserValidator validator = CreateUserValidator.getInstance();

    @Test
    void whenDtoIsValid_thenValidationSuccessful() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Mike Pierson")
                .birthday("2001-01-01")
                .email("test@gmail.com")
                .password("test")
                .role(Role.USER.name())
                .gender(Gender.MALE.name())
                .build();

        ValidationResult actual = validator.validate(dto);

        assertThat(actual.isValid()).isTrue();
    }

    @Test
    void whenBirthdayIsInvalid_thenValidationFails() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Mike Pierson")
                .birthday("201-01-01")
                .email("test@gmail.com")
                .password("test")
                .role(Role.USER.name())
                .gender(Gender.MALE.name())
                .build();

        ValidationResult actual = validator.validate(dto);

        assertThat(actual.isValid()).isFalse();
        assertThat(actual.getErrors().get(0).getMessage()).isEqualTo("Birthday is invalid");
    }

    @Test
    void whenRoleIsInvalid_thenValidationFails() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Mike Pierson")
                .birthday("2001-01-01")
                .email("test@gmail.com")
                .password("test")
                .role("invalid role")
                .gender(Gender.MALE.name())
                .build();

        ValidationResult actual = validator.validate(dto);

        assertThat(actual.isValid()).isFalse();
        assertThat(actual.getErrors().get(0).getMessage()).isEqualTo("Role is invalid");
    }

    @Test
    void whenGenderIsInvalid_thenValidationFails() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Mike Pierson")
                .birthday("2001-01-01")
                .email("test@gmail.com")
                .password("test")
                .role(Role.USER.name())
                .gender("invalid gender")
                .build();

        ValidationResult actual = validator.validate(dto);

        assertThat(actual.isValid()).isFalse();
        assertThat(actual.getErrors().get(0).getMessage()).isEqualTo("Gender is invalid");
    }

    @Test
    void whenCheckedFieldsAreInvalid_thenValidationFails() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Mike Pierson")
                .birthday("invalid birthday")
                .email("test@gmail.com")
                .password("test")
                .role("invalid role")
                .gender("invalid gender")
                .build();

        ValidationResult actual = validator.validate(dto);

        assertThat(actual.isValid()).isFalse();
        assertThat(actual.getErrors().stream().map(e -> e.getCode()).toList()).contains(104, 105, 106);
    }
}
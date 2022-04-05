package com.dmdev.integration.service;

import com.dmdev.dao.UserDao;
import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.exception.ValidationException;
import com.dmdev.integration.IntegrationTestBase;
import com.dmdev.mapper.CreateUserMapper;
import com.dmdev.mapper.UserMapper;
import com.dmdev.service.UserService;
import com.dmdev.validator.CreateUserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.dmdev.integration.util.TestObjectUtils.IVAN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceIT extends IntegrationTestBase {

    private UserService userService;

    @BeforeEach
    void init() {
        userService = new UserService(
                CreateUserValidator.getInstance(),
                UserDao.getInstance(),
                CreateUserMapper.getInstance(),
                UserMapper.getInstance()
        );
    }

    @Test
    void shouldLoginSuccessfully() {
        Optional<UserDto> actualResult = userService.login(IVAN.getEmail(), IVAN.getPassword());

        assertThat(actualResult).isPresent();
        assertEquals(actualResult.get().getEmail(), IVAN.getEmail());
    }

    @Test
    void shouldNotLoginIfPasswordIncorrect() {
        Optional<UserDto> actualResult = userService.login(IVAN.getEmail(), "dummy");

        assertThat(actualResult).isEmpty();
    }

    @Test
    void shouldCreateCorrectEntity() {
        CreateUserDto userToCreate = CreateUserDto.builder()
                .name("Test")
                .birthday("2020-01-01")
                .email("test@gmail.com")
                .password("'123'")
                .role("USER")
                .gender("MALE")
                .build();

        UserDto createdUser = userService.create(userToCreate);

        assertNotNull(createdUser.getId());
    }

    @Test
    void shouldThrowValidationExceptionIfEntityInvalid() {
        CreateUserDto userToCreate = CreateUserDto.builder()
                .name("Test")
                .birthday("01-01-2019")
                .email("test@gmail.com")
                .password("'123'")
                .role("USER")
                .gender("MALE")
                .build();

        var actualException = assertThrows(ValidationException.class, () -> userService.create(userToCreate));
        assertThat(actualException.getErrors()).hasSize(1);
        assertThat(actualException.getErrors().get(0).getCode()).isEqualTo("invalid.birthday");
    }
}
package com.dmdev.integration;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.exception.ValidationException;
import com.dmdev.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("UserService integration tests")
@Tag("UserService")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceIT extends IntegrationTestBase{

    private static final UserService userService = UserService.getInstance();
    private static final UserDto IVAN = UserDto.builder().birthday(LocalDate.of(1990, 1, 10))
            .email("ivan@gmail.com").gender(Gender.MALE).name("Ivan").id(1).role(Role.ADMIN).build();

    @Nested
    @Tag("LoginUser")
    @DisplayName("Login user functionality")
    class LoginUser {
        @Test
        void loginSuccess() {
            String password = "111";
            assertThat(userService.login(IVAN.getEmail(), password)).isPresent().hasValue(IVAN);
        }

        @Test
        void loginFailedWithIncorrectPassword() {
            String incorrectPassword = "112";
            assertThat(userService.login(IVAN.getEmail(), incorrectPassword)).isEmpty();
        }
    }

    @Nested
    @Tag("CreateUser")
    @DisplayName("Create user functionality")
    class CreateUser {

        @Test
        void createSuccess() {
            CreateUserDto inputNewUser = CreateUserDto.builder().role(Role.USER.name())
                    .email("petro@gmail.com").name("Petro").gender(Gender.MALE.name()).password("123")
                    .birthday(LocalDate.of(1986, 1, 10).toString()).build();

            UserDto expectedUser = UserDto.builder().birthday(LocalDate.of(1986, 1, 10))
                    .name("Petro").email("petro@gmail.com").gender(Gender.MALE)
                    .id(6).role(Role.USER).build();
            assertThat(userService.create(inputNewUser)).isEqualTo(expectedUser);
        }

        @Test
        void createFailedWithIncorrectUserBirthDate() {
            CreateUserDto incorrectNewUser = CreateUserDto.builder().role(Role.USER.name())
                    .email("petro@gmail.com").name("Petro").gender(Gender.MALE.name()).password("123")
                    .birthday("20-02-11111").build();
            assertThrows(ValidationException.class, () -> userService.create(incorrectNewUser));
        }

        @Test
        void createFailedWithIncorrectGender() {
            CreateUserDto incorrectNewUser = CreateUserDto.builder().role(Role.USER.name())
                    .email("petro@gmail.com").name("Petro").gender("no").password("123")
                    .birthday(LocalDate.of(1986, 1, 10).toString()).build();
            assertThrows(ValidationException.class, () -> userService.create(incorrectNewUser));
        }

        @Test
        void createFailedWithIncorrectUserRole() {
            CreateUserDto incorrectNewUser = CreateUserDto.builder().role("god")
                    .email("petro@gmail.com").name("Petro").gender(Gender.MALE.name()).password("123")
                    .birthday(LocalDate.of(1986, 1, 10).toString()).build();
            assertThrows(ValidationException.class, () -> userService.create(incorrectNewUser));
        }
    }
}

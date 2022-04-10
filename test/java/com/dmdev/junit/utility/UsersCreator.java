package com.dmdev.junit.utility;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class UsersCreator {

    public static CreateUserDto getCreateUserInvalidGender() {
        return CreateUserDto.builder()
                .name("name")
                .birthday("1999-01-01")
                .email("email")
                .password("password")
                .gender("dummy")
                .role(String.valueOf(Role.USER))
                .build();
    }

    public static CreateUserDto getCreateUserInvalidRole() {
        return CreateUserDto.builder()
                .name("name")
                .birthday("1999-01-01")
                .email("email")
                .password("password")
                .gender(String.valueOf(Gender.MALE))
                .role("dummy")
                .build();
    }

    public static CreateUserDto getCreateUserInvalidBirthday() {
        return CreateUserDto.builder()
                .name("name")
                .birthday("")
                .email("email")
                .password("password")
                .gender(String.valueOf(Gender.MALE))
                .role(String.valueOf(Role.USER))
                .build();
    }

    public static void getCreateUserInvalidAll() {
        CreateUserDto.builder()
                .name("name")
                .birthday("")
                .email("email")
                .password("password")
                .gender("dummy")
                .role("dummy")
                .build();
    }

    public static CreateUserDto getCreateUserValid() {
        return CreateUserDto.builder()
                .name("name")
                .birthday("1999-01-01")
                .email("email")
                .password("password")
                .gender(String.valueOf(Gender.MALE))
                .role(String.valueOf(Role.USER))
                .build();
    }

    public static User getUserValid() {
        return User.builder()
                .id(2)
                .name("name")
                .birthday(LocalDate.of(1999, 1, 1))
                .gender(Gender.MALE)
                .email("dummy")
                .password("dummy")
                .role(Role.USER)
                .build();
    }
}

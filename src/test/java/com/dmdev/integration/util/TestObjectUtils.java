package com.dmdev.integration.util;

import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

/**
 * В разных тестовых классах часто используется одна и та же модель и даже методы.
 * Особенно часто выносятся в константы данные из тестовых SQL скриптов.
 *
 * Поэтому проще и лучше выносить все общее в отдельные тестовые утилитные классы
 */
@UtilityClass
public class TestObjectUtils {

    public static final User IVAN = User.builder()
            .id(1)
            .name("Ivan")
            .birthday(LocalDate.of(1990, 1, 10))
            .email("ivan@gmail.com")
            .password("111")
            .role(Role.ADMIN)
            .gender(Gender.MALE)
            .build();

    public static final User PETR = User.builder()
            .id(2)
            .name("Petr")
            .birthday(LocalDate.of(1995, 10, 19))
            .email("petr@gmail.com")
            .password("123")
            .role(Role.USER)
            .gender(Gender.MALE)
            .build();
}

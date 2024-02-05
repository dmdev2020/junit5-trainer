package com.dmdev.mapper;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CreateUserMapperTest {

    private static final CreateUserMapper mapper = CreateUserMapper.getInstance();

    @Test
    void whenDtoIsValid_thenUserIsValid() {

        CreateUserDto validDto = CreateUserDto.builder()
                .name("Mike Pierson")
                .birthday("2001-01-01")
                .email("test@gmail.com")
                .password("test")
                .role(Role.USER.name())
                .gender(Gender.MALE.name())
                .build();

        User actualUser = mapper.map(validDto);

        User validUser = User.builder()
                .name("Mike Pierson")
                .birthday(LocalDate.of(2001, 1, 1))
                .email("test@gmail.com")
                .password("test")
                .role(Role.USER)
                .gender(Gender.MALE)
                .build();

        assertThat(actualUser).isEqualTo(validUser);
    }
}
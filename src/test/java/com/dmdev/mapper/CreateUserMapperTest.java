package com.dmdev.mapper;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class CreateUserMapperTest {

    private final CreateUserMapper mapper = CreateUserMapper.getInstance();

    @Test
    void shouldMapAllFieldsCorrectly() {
        CreateUserDto dto = CreateUserDto.builder()
                .email("test@gmail.com")
                .password("pass")
                .name("Test")
                .birthday("2000-01-02")
                .gender(Gender.FEMALE.name())
                .role(Role.USER.name())
                .build();

        User actualResult = mapper.map(dto);

        assertEquals(dto.getEmail(), actualResult.getEmail());
        assertEquals(dto.getPassword(), actualResult.getPassword());
        assertEquals(dto.getName(), actualResult.getName());
        assertEquals(LocalDate.of(2000, 1, 2), actualResult.getBirthday());
        assertSame(Gender.valueOf(dto.getGender()), actualResult.getGender());
        assertSame(Role.valueOf(dto.getRole()), actualResult.getRole());
    }

}
package com.dmdev.mapper;

import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class UserMapperTest {

    private final UserMapper mapper = UserMapper.getInstance();

    @Test
    void shouldMapAllFieldsCorrectly() {
        User user = User.builder()
                .id(1)
                .email("test@gmail.com")
                .password("pass")
                .name("Test")
                .birthday(LocalDate.of(2000, 5, 18))
                .gender(Gender.FEMALE)
                .role(Role.USER)
                .build();

        UserDto actualResult = mapper.map(user);

        assertEquals(user.getId(), actualResult.getId());
        assertEquals(user.getEmail(), actualResult.getEmail());
        assertEquals(user.getName(), actualResult.getName());
        assertEquals(user.getBirthday(), actualResult.getBirthday());
        assertSame(user.getGender(), actualResult.getGender());
        assertSame(user.getRole(), actualResult.getRole());
    }

}
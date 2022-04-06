package com.dmdev.mapper;

import com.dmdev.generator.UserGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    private final UserMapper userMapper = UserMapper.getInstance();

    @Test
    void mapUserToUserDto() {

        var actualResult = userMapper.map(UserGenerator.getUser());
        var expectedResult = UserGenerator.getUserDto();

        assertEquals(actualResult.getId(), expectedResult.getId());
        assertEquals(actualResult.getName(), expectedResult.getName());
        assertEquals(actualResult.getBirthday(), expectedResult.getBirthday());
        assertEquals(actualResult.getGender(), expectedResult.getGender());
        assertEquals(actualResult.getEmail(), expectedResult.getEmail());
        assertEquals(actualResult.getRole(), expectedResult.getRole());
    }
}

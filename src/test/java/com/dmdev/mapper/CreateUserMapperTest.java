package com.dmdev.mapper;

import com.dmdev.generator.UserGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateUserMapperTest {

    private final CreateUserMapper createUserMapper = CreateUserMapper.getInstance();

    @Test
    void mapCreateUserDtoToUser() {
        var actualResult = createUserMapper.map(UserGenerator.getCreateUserDto());
        var expectedResult = UserGenerator.getUser();

        assertEquals(actualResult.getName(),expectedResult.getName());
        assertEquals(actualResult.getBirthday(),expectedResult.getBirthday());
        assertEquals(actualResult.getEmail(),expectedResult.getEmail());
        assertEquals(actualResult.getPassword(),expectedResult.getPassword());
        assertEquals(actualResult.getRole(),expectedResult.getRole());
        assertEquals(actualResult.getGender(),expectedResult.getGender());
    }
}

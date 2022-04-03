package com.dmdev.unit;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserTestDto;
import com.dmdev.entity.User;
import com.dmdev.mapper.CreateUserMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateUserMapperTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/users.csv", delimiter = ',', numLinesToSkip = 1)
    void mapUser(String id, String name, String birthDay, String email,
                 String password, String role, String gender) {
        UserTestDto testDto = UserTestDto.of(id, name, birthDay, email, password, role, gender);
        CreateUserDto createUserDto = CreateUserDto.builder()
                .password(password)
                .name(name)
                .email(email)
                .gender(gender)
                .role(role)
                .birthday(birthDay)
                .build();
        User expected = User.builder()
                .role(testDto.getRole())
                .name(testDto.getName())
                .email(testDto.getEmail())
                .gender(testDto.getGender())
                .birthday(testDto.getBirthday())
                .password(testDto.getPassword())
                .build();

        CreateUserMapper mapper = CreateUserMapper.getInstance();
        User result = mapper.map(createUserDto);
        assertThat(result).isEqualTo(expected);
    }
}

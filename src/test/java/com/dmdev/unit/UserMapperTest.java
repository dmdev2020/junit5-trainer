package com.dmdev.unit;

import com.dmdev.dto.UserDto;
import com.dmdev.dto.UserTestDto;
import com.dmdev.entity.User;
import com.dmdev.mapper.UserMapper;
import com.dmdev.util.UserWorkerUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testing UserMapper")
@Tag("userMapper")
public class UserMapperTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/users.csv", delimiter = ',', numLinesToSkip = 1)
    void mapUser(String id, String name, String birthDay, String email,
                 String password, String role, String gender) {
        UserTestDto testDto = UserTestDto.of(id, name, birthDay, email, password, role, gender);
        UserDto expected = UserDto.builder()
                .name(testDto.getName())
                .gender(testDto.getGender())
                .email(testDto.getEmail())
                .role(testDto.getRole())
                .id(testDto.getId())
                .birthday(testDto.getBirthday())
                .build();

        User user = UserWorkerUtil.castTestUserToUserEntity(testDto);
        UserMapper mapper = UserMapper.getInstance();
        UserDto result = mapper.map(user);
        assertThat(result).isEqualTo(expected);
    }
}

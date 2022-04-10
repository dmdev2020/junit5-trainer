package com.dmdev.junit.mapper;

import com.dmdev.junit.utility.UsersCreator;
import com.dmdev.mapper.UserMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMapperTest {

    private final UserMapper userMapper = UserMapper.getInstance();

    @Test
    void userMapperPositive() {
        var originalUser = UsersCreator.getUserValid();
        var result = userMapper.map(UsersCreator.getUserValid());

        assertThat(result.getGender()).isEqualTo(originalUser.getGender());
        assertThat(result.getRole()).isEqualTo(originalUser.getRole());
        assertThat(result.getBirthday()).isEqualTo(originalUser.getBirthday());
        assertThat(result.getName()).isEqualTo(originalUser.getName());
        assertThat(result.getEmail()).isEqualTo(originalUser.getEmail());
        assertThat(result.getId()).isEqualTo(originalUser.getId());
    }
}

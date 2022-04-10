package com.dmdev.junit.mapper;

import com.dmdev.junit.utility.UsersCreator;
import com.dmdev.mapper.CreateUserMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateUserMapperTest {

    private final CreateUserMapper createUserMapper = CreateUserMapper.getInstance();

    @Test
    void createUserMapperPositive() {
        var originalUser = UsersCreator.getCreateUserValid();
        var result = createUserMapper.map(originalUser);

        assertThat(result.getGender().toString()).isEqualTo(originalUser.getGender());
        assertThat(result.getRole().toString()).isEqualTo(originalUser.getRole());
        assertThat(result.getBirthday().toString()).isEqualTo(originalUser.getBirthday());
        assertThat(result.getName()).isEqualTo(originalUser.getName());
        assertThat(result.getPassword()).isEqualTo(originalUser.getPassword());
        assertThat(result.getEmail()).isEqualTo(originalUser.getEmail());
    }
}

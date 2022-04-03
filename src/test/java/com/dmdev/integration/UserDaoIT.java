package com.dmdev.integration;

import com.dmdev.dao.UserDao;
import com.dmdev.dto.UserTestDto;
import com.dmdev.entity.User;
import com.dmdev.util.UserFileReaderUtil;
import com.dmdev.util.UserWorkerUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserDao integration tests")
@Tag("userDao")
public class UserDaoIT extends IntegrationTestBase {
    private static final UserDao userDao = UserDao.getInstance();

    @ParameterizedTest
    @CsvFileSource(resources = "/users.csv", delimiter = ',', numLinesToSkip = 1)
    @DisplayName("find existed user by id")
    void findExistedUserById(String id, String name, String birthDay, String email,
                      String password, String role, String gender) {
        Integer userId = Integer.parseInt(id);
        UserTestDto inputUser = UserTestDto.of(id, name, birthDay, email, password, role, gender);
        assertThat(userDao.findById(userId)).isPresent().matches(
                user -> UserWorkerUtil.userEqualUserTestDto(inputUser, user.get())
        );
    }

    @Test
    @DisplayName("find not existed user by id")
    void findNotExistedUserById() {
        assertThat(userDao.findById(100)).isEmpty();
    }

    @Test
    @DisplayName("find all users")
    void findAllUsers() {
        List<UserTestDto> testUsers = UserFileReaderUtil.readAllTestUsersFromCsv("users.csv");
        assertThat(userDao.findAll())
                .isNotEmpty()
                .allMatch(user -> testUsers.stream().anyMatch(
                expectedUser -> UserWorkerUtil.userEqualUserTestDto(expectedUser, user))
        );
    }

    @Test
    void deleteUserSuccess() {
        assertThat(userDao.delete(1)).isTrue();
    }

    @Test
    void deleteUserFailed() {
        assertThat(userDao.delete(100)).isFalse();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/users.csv", delimiter = ',', numLinesToSkip = 1)
    @DisplayName("update success")
    void updateSuccess(String id, String name, String birthDay, String email,
                String password, String role, String gender) {
        UserTestDto inputUser = UserTestDto.of(id, name+"'s", birthDay, email, password, role, gender);
        User inputUserEntity = UserWorkerUtil.castTestUserToUserEntity(inputUser);
        userDao.update(inputUserEntity);
        User resultUser = userDao.findById(inputUser.getId()).get();
        assertThat(resultUser).isEqualTo(inputUserEntity);
    }
}

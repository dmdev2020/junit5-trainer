package com.dmdev.junit.integration.dao;

import com.dmdev.dao.UserDao;
import com.dmdev.integration.IntegrationTestBase;
import com.dmdev.junit.utility.UsersCreator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoIT extends IntegrationTestBase {

    private final UserDao userDao = UserDao.getInstance();

    @Test
    void userDaoFindAllPositive() {
        var result = userDao.findAll();
        assertThat(result).hasSize(5);
    }

    @Test
    void userDaoFindByIdPositive() {
        var result = userDao.findById(1);
        assertThat(result).isPresent();
    }

    @Test
    void userDaoFindByIdNegativeUnexistedId() {
        var result = userDao.findById(9999);
        assertThat(result).isEmpty();
    }

    @Test
    void userDaoSavePositive() {
        var originalUser = UsersCreator.getUserValid();
        var result = userDao.save(originalUser);

        assertThat(result).isEqualTo(originalUser);
    }

    @Test
    void userDaoDeletePositive() {
        var actualResult = userDao.delete(5);
        assertThat(actualResult).isTrue();
    }

    @Test
    void userDaoDeleteNegativeUnexistedId() {
        var actualResult = userDao.delete(999);
        assertThat(actualResult).isFalse();
    }
}

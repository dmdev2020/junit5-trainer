package com.dmdev.integration.dao;

import com.dmdev.dao.UserDao;
import com.dmdev.generator.UserGenerator;
import com.dmdev.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class UserDaoIT extends IntegrationTestBase {

    private final UserDao userDao = UserDao.getInstance();

    @Test
    void userFindById() {
        var actualResult = userDao.findById(1);
        assertThat(actualResult).isPresent();
    }

    @Test
    void userNotExist() {
        var actualResult = userDao.findById(1543);
        assertThat(actualResult).isEmpty();
    }

    @Test
    void findAllUsers() {
        var actualResult = userDao.findAll();
        assertThat(actualResult.size()).isEqualTo(5);
    }

    @Test
    void shouldDeleteExistedUser() {
        var actualResult = userDao.delete(1);
        assertThat(actualResult).isTrue();
    }

    @Test
    void updateExistedUser() {
        userDao.update(UserGenerator.getUser());
        var actualResult = userDao.findById(UserGenerator.getUser().getId());
        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(UserGenerator.getUser());
    }

    @Test
    void findByExistEmailAndPassword() {
        var actualResult = userDao.findByEmailAndPassword("sveta@gmail.com", "321");
        assertThat(actualResult).isPresent();
    }

    @Test
    void findByExistEmailButIsNotCorrectPassword() {
        var actualResult = userDao.findByEmailAndPassword("sveta@gmail.com", Mockito.any());
        assertThat(actualResult).isEmpty();
    }
}

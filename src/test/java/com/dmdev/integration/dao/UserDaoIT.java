package com.dmdev.integration.dao;

import com.dmdev.dao.UserDao;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static com.dmdev.integration.util.TestObjectUtils.IVAN;
import static com.dmdev.integration.util.TestObjectUtils.PETR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDaoIT extends IntegrationTestBase {

    /*
     * Почти всегда в тестах в качестве полей выносится объект тестируемого класса:
     * - добавляет наглядности и прозрачности, что именно тестируем
     * - не нужно дублировать создание объекта в каждом методе
     * - можно легко настраивать, а также использовать Mock/Spy (например, в @BeforeEach или MockitoExtension)
     */
    private final UserDao userDao = UserDao.getInstance();

    @Test
    void shouldFindAll() {
        assertThat(userDao.findAll()).hasSize(5);
    }

    @Test
    void shouldFindExistingEntity() {
        Optional<User> actualResult = userDao.findById(IVAN.getId());

        assertThat(actualResult).isPresent();
        assertEquals(IVAN.getId(), actualResult.get().getId());
    }

    @Test
    void shouldReturnEmptyIfEntityDoesNotExist() {
        Optional<User> actualResult = userDao.findById(999);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void shouldSaveCorrectEntity() {
        User user = User.builder()
                .email("test@gmail.com")
                .password("qwerty")
                .birthday(LocalDate.of(1995, 8, 15))
                .name("Test")
                .gender(Gender.MALE)
                .role(Role.ADMIN)
                .build();

        userDao.save(user);

        assertNotNull(user.getId());
        assertEquals(user, userDao.findById(user.getId()).get());
    }

    @Test
    void shouldUpdateExistingEntity() {
        User expectedUser = userDao.findById(IVAN.getId()).get();
        expectedUser.setEmail("new@gmail.com");
        expectedUser.setPassword("secret");
        expectedUser.setBirthday(LocalDate.now());
        expectedUser.setName("test");
        expectedUser.setGender(Gender.FEMALE);
        expectedUser.setRole(Role.USER);

        userDao.update(expectedUser);

        User actualUser = userDao.findById(IVAN.getId()).get();
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void shouldDeleteExistingEntity() {
        assertTrue(userDao.delete(IVAN.getId()));
    }

    @Test
    void shouldNotDeleteNotExistingEntity() {
        assertFalse(userDao.delete(999));
    }

    @Test
    void shouldFindExistingUserByEmailAndPassword() {
        Optional<User> actualResult = userDao.findByEmailAndPassword(PETR.getEmail(), PETR.getPassword());

        assertThat(actualResult).isPresent();
        assertEquals(PETR.getEmail(), actualResult.get().getEmail());
    }

    @Test
    void shouldNotFindExistingUserWithWrongPassword() {
        Optional<User> actualResult = userDao.findByEmailAndPassword(PETR.getEmail(), "dummy");

        assertThat(actualResult).isEmpty();
    }

    @Test
    void shouldNotFindExistingUserWithWrongEmail() {
        Optional<User> actualResult = userDao.findByEmailAndPassword("dummy", PETR.getPassword());

        assertThat(actualResult).isEmpty();
    }
}
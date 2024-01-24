package unit;

import com.dmdev.dao.UserDao;
import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.mapper.CreateUserMapper;
import com.dmdev.mapper.UserMapper;
import com.dmdev.service.UserService;
import com.dmdev.validator.CreateUserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @Mock
    private static UserService INSTANCE;

    @Mock
    private CreateUserValidator createUserValidator;
    @Mock
    private UserDao userDao;
    @Mock
    private CreateUserMapper createUserMapper;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;

    @Test
    void login_ValidCredentials_ReturnsUserDto() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        User user = new User();
        user.setId(1);
        user.setName("Aaa");
        user.setGender(Gender.MALE);
        user.setEmail(email);
        user.setPassword(password);
        UserDto expectedUserDto = UserDto.builder()
                .id(1)
                .image("htp")
                .email(email)
                .gender(Gender.MALE)
                .name("Aaa")
                        .build();
        doReturn(Optional.of(user)).when(userDao).findByEmailAndPassword(email,password);
        doReturn(expectedUserDto).when(userMapper).map(any());

        // Act
        Optional<UserDto> result = userService.login(email, password);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedUserDto, result.get());
//        verify(userDao, times(1)).findByEmailAndPassword(email, password);
//        verify(userMapper, times(1)).map(any());
    }
}
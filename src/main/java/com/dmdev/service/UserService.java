package com.dmdev.service;

import com.dmdev.dao.UserDao;
import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.exception.ValidationException;
import com.dmdev.mapper.CreateUserMapper;
import com.dmdev.mapper.UserMapper;
import com.dmdev.validator.CreateUserValidator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.Optional;

@RequiredArgsConstructor
public class UserService {

    private final CreateUserValidator createUserValidator;
    private final UserDao userDao;
    private final CreateUserMapper createUserMapper;
    private final UserMapper userMapper;

    public Optional<UserDto> login(String email, String password) {
        return userDao.findByEmailAndPassword(email, password)
                .map(userMapper::map);
    }

    @SneakyThrows
    public UserDto create(CreateUserDto userDto) {
        var validationResult = createUserValidator.validate(userDto);
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getErrors());
        }
        var userEntity = createUserMapper.map(userDto);
        userDao.save(userEntity);

        return userMapper.map(userEntity);
    }
}

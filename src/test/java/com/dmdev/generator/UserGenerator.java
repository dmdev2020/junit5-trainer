package com.dmdev.generator;


import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class UserGenerator {

    public static CreateUserDto getCreateUserDto(){
        return CreateUserDto.builder()
                .name("Kate")
                .birthday("1989-08-09")
                .email("kate@gmail.com")
                .password("777")
                .role("ADMIN")
                .gender("FEMALE")
                .build();
    }

    public static User getUser(){
        return User.builder()
                .id(5)
                .name("Kate")
                .birthday(LocalDate.of(1989,8,9))
                .gender(Gender.FEMALE)
                .email("kate@gmail.com")
                .password("777")
                .role(Role.ADMIN)
                .build();
    }

    public static CreateUserDto getNotCorrectUser(){
        return CreateUserDto.builder()
                .name("Vasya")
                .birthday("01-10-1990")
                .gender("Double sexual")
                .role("Guest")
                .email("vasya@gmail.com")
                .password("111")
                .build();
    }

    public static CreateUserDto getCorrectUser(){
        return CreateUserDto.builder()
                .name("Vasya")
                .birthday("1990-01-10")
                .gender("MALE")
                .role("USER")
                .email("vasya@gmail.com")
                .password("111")
                .build();
    }

    public static UserDto getUserDto(){
        return UserDto.builder()
                .id(5)
                .name("Kate")
                .birthday(LocalDate.of(1989,8,9))
                .gender(Gender.FEMALE)
                .email("kate@gmail.com")
                .role(Role.ADMIN)
                .image("image")
                .build();
    }
}

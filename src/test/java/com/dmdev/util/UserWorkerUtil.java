package com.dmdev.util;

import com.dmdev.dto.UserTestDto;
import com.dmdev.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserWorkerUtil {

    public boolean userEqualUserTestDto(UserTestDto userTestDto, User user) {
        return userTestDto.getBirthday().equals(user.getBirthday())
                && userTestDto.getEmail().equals(user.getEmail())
                && userTestDto.getName().equals(user.getName())
                && userTestDto.getRole().equals(user.getRole())
                && userTestDto.getGender().equals(user.getGender())
                && userTestDto.getPassword().equals(user.getPassword())
                && userTestDto.getId().equals(user.getId());
    }

    public User castTestUserToUserEntity(UserTestDto userTestDto) {
        return User.builder()
                .id(userTestDto.getId())
                .birthday(userTestDto.getBirthday())
                .email(userTestDto.getEmail())
                .name(userTestDto.getName())
                .gender(userTestDto.getGender())
                .role(userTestDto.getRole())
                .password(userTestDto.getPassword())
                .build();
    }
}

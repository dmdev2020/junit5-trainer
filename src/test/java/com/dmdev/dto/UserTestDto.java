package com.dmdev.dto;

import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import lombok.Value;

import java.time.LocalDate;


@Value
public class UserTestDto {
    Integer id;
    String name;
    LocalDate birthday;
    String email;
    String password;
    Role role;
    Gender gender;

    private UserTestDto(Integer id, String name, LocalDate birthday, String email, String password, Role role, Gender gender) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
        this.role = role;
        this.gender = gender;
    }

    public static UserTestDto of(String id, String name, String birthday, String email, String password, String role, String gender) {
        Integer convertedId = Integer.parseInt(id);
        LocalDate convertedBirthday = LocalDate.parse(birthday);
        Role convertedRole = role != null ? Role.valueOf(role) : null;
        Gender convertedGender = gender != null ? Gender.valueOf(gender) : null;
        return new UserTestDto(convertedId, name, convertedBirthday, email, password, convertedRole, convertedGender);
    }
}

package com.dmdev.service;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.integration.IntegrationTestBase;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserServiceTest extends IntegrationTestBase {

	@Test
	void login() {
		Assertions.assertThat(UserService.getInstance().login("ivan@gmail.com", "111")).isPresent();
		Assertions.assertThat(UserService.getInstance().login("ivan@gmail.com", "dummy")).isEmpty();
		Assertions.assertThat(UserService.getInstance().login("dummy", "111")).isEmpty();

	}

	@Test
	void create() {
		UserDto userDto = UserDto.builder()
				.name("Ivan")
				.birthday(LocalDate.parse("1990-01-10"))
				.email("ivan_new@gmail.com")
				.gender(Gender.MALE)
				.role(Role.ADMIN)
				.build();

		CreateUserDto createUserDto = CreateUserDto.builder()
				.name("Ivan")
				.birthday("1990-01-10")
				.email("ivan_new@gmail.com")
				.gender(Gender.MALE.toString())
				.role(Role.ADMIN.toString())
				.password("111")
				.build();

		UserDto newUserDto = UserService.getInstance().create(createUserDto);

		Assertions.assertThat(userDto.getName()).isEqualTo(newUserDto.getName());
		Assertions.assertThat(userDto.getBirthday()).isEqualTo(newUserDto.getBirthday());
		Assertions.assertThat(userDto.getEmail()).isEqualTo(newUserDto.getEmail());
		Assertions.assertThat(userDto.getGender()).isEqualTo(newUserDto.getGender());
		Assertions.assertThat(userDto.getRole()).isEqualTo(newUserDto.getRole());

	}

}

package com.dmdev.mapper;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class CreateUserMapperTest {

	@Test
	void map() {
		User user = User.builder()
				.name("Ivan")
				.birthday(LocalDate.parse("1990-01-10"))
				.email("ivan@gmail.com")
				.password("111")
				.role(Role.ADMIN)
				.gender(Gender.MALE)
				.build();

		CreateUserDto dto = CreateUserDto.builder()
				.name("Ivan")
				.birthday("1990-01-10")
				.email("ivan@gmail.com")
				.gender(Gender.MALE.toString())
				.role(Role.ADMIN.toString())
				.password("111")
				.build();

		Assertions.assertThat(CreateUserMapper.getInstance().map(dto))
				.isEqualTo(user);
	}

}

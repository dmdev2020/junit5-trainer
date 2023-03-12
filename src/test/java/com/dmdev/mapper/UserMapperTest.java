package com.dmdev.mapper;

import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserMapperTest {

	@Test
	void map() {
		User user = User.builder()
				.id(1)
				.name("Ivan")
				.birthday(LocalDate.parse("1990-01-10"))
				.email("ivan@gmail.com")
				.password("111")
				.role(Role.ADMIN)
				.gender(Gender.MALE)
				.build();

		UserDto dto = UserDto.builder()
				.id(1)
				.name("Ivan")
				.birthday(LocalDate.parse("1990-01-10"))
				.email("ivan@gmail.com")
				.gender(Gender.MALE)
				.role(Role.ADMIN)
				.build();

		Assertions.assertThat(UserMapper.getInstance().map(user))
				.isEqualTo(dto);
	}

}

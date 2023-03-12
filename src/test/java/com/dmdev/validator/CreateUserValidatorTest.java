package com.dmdev.validator;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class CreateUserValidatorTest {

	@ParameterizedTest
	@MethodSource("getArgumentsForValidateTest")
	void validate(CreateUserDto dto, boolean result) {
		ValidationResult validationResult = CreateUserValidator.getInstance().validate(dto);
		Assertions.assertThat(validationResult.isValid()).isEqualTo(result);
	}

	static Stream<Arguments> getArgumentsForValidateTest() {
		return Stream.of(
				Arguments.of(CreateUserDto.builder()
						.name("Ivan")
						.birthday("1990-01-10")
						.email("ivan@gmail.com")
						.gender(Gender.MALE.toString())
						.role(Role.ADMIN.toString())
						.password("111")
						.build(), true),
				Arguments.of(CreateUserDto.builder()
						.name("Ivan")
						.birthday("199-01-10")
						.email("ivan@gmail.com")
						.gender(Gender.MALE.toString())
						.role(Role.ADMIN.toString())
						.password("111")
						.build(), false),
				Arguments.of(CreateUserDto.builder()
						.name("Ivan")
						.birthday("1990-01-10")
						.email("ivan@gmail.com")
						.gender("dummy")
						.role(Role.ADMIN.toString())
						.password("111")
						.build(), false),
				Arguments.of(CreateUserDto.builder()
						.name("Ivan")
						.birthday("1990-01-10")
						.email("ivan@gmail.com")
						.gender(Gender.MALE.toString())
						.role("dummy")
						.password("111")
						.build(), false)
		);
	}

}

package com.dmdev.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LocalDateFormatterTest {

	private static final String PATTERN = "yyyy-MM-dd";

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

	@ParameterizedTest
	@MethodSource("com.dmdev.util.LocalDateFormatterTest#getArgumentsForFormatTest")
	void format(String date, LocalDate localDate) {
		Assertions.assertThat(LocalDateFormatter.format(date))
				.isEqualTo(localDate);
	}

	static Stream<Arguments> getArgumentsForFormatTest() {
		System.out.println("test");
		return Stream.of(
				Arguments.of("2001-01-01", LocalDate.parse("2001-01-01", FORMATTER)),
				Arguments.of("2020-05-07", LocalDate.parse("2020-05-07", FORMATTER)),
				Arguments.of("2023-08-01", LocalDate.parse("2023-08-01", FORMATTER))
		);
	}

}

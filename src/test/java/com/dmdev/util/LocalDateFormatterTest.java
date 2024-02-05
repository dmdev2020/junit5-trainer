package com.dmdev.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class LocalDateFormatterTest {

    @Test
    void whenValidDate_thenParsedSuccessfully() {
        LocalDate actual = LocalDateFormatter.parse("2009-03-25");
        LocalDate expected = LocalDate.of(2009, 3, 25);
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void whenInValidDate_thenExceptionThrown() {
        assertThatThrownBy(() -> LocalDateFormatter.parse("209-03-25"));
        assertThatThrownBy(() -> LocalDateFormatter.parse(""));
    }

    @ParameterizedTest
    @MethodSource("getDatesTestCases")
    void checkIfDateStringsValidOrNot(String date, boolean expected) {
        boolean actual = LocalDateFormatter.isValid(date);
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> getDatesTestCases() {
        return Stream.of(
                arguments("2009-01-01", true),
                arguments("2009-13-13", false),
                arguments("", false)
        );
    }
}
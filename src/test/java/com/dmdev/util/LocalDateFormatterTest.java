package com.dmdev.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void isValid() {
    }
}
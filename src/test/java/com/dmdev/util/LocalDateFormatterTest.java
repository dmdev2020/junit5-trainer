package com.dmdev.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class LocalDateFormatterTest {

    @Test
    void whenValidDate_thenParsedSuccessfully() {
        LocalDate actual = LocalDateFormatter.parse("2009-03-25");
        LocalDate expected = LocalDate.of(2009, 3, 25);
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void isValid() {
    }
}
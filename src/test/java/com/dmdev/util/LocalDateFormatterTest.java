package com.dmdev.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalDateFormatterTest {

    @Test
    void shouldFormatLocalDateCorrectly() {
        var sourceDate = "2019-05-13";

        var actualResult = LocalDateFormatter.format(sourceDate);

        assertThat(actualResult).isEqualTo(LocalDate.of(2019, 5, 13));
    }

    @Test
    void shouldThrowExceptionIfDateIsInvalid() {
        var invalidDate = "05-13-2020";

        assertThrows(DateTimeParseException.class, () -> LocalDateFormatter.format(invalidDate));
    }

    @Test
    void shouldReturnTrueIfDateIsValid() {
        var sourceDate = "2019-05-13";

        assertTrue(LocalDateFormatter.isValid(sourceDate));
    }

    @Test
    void shouldReturnFalseIfDateIsInvalid() {
        var sourceDate = "dummy";

        assertFalse(LocalDateFormatter.isValid(sourceDate));
    }

    @Test
    void shouldReturnFalseIfDateIsNull() {
        assertFalse(LocalDateFormatter.isValid(null));
    }
}
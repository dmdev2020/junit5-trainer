package com.dmdev.validator;

import com.dmdev.dto.CreateSubscriptionDto;
import com.dmdev.entity.Provider;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class CreateSubscriptionValidatorTest {

    private static final CreateSubscriptionValidator validator = CreateSubscriptionValidator.getInstance();

    @Test
    void whenValidDto_thenValidationSuccessful() {
        CreateSubscriptionDto validDto = CreateSubscriptionDto.builder()
                .userId(1)
                .name("Test Name")
                .provider(Provider.APPLE.name())
                .expirationDate(getExpirationDate("21:00:00, 30.04.2024", "H:m:s, d.M.y"))
                .build();

        ValidationResult actualResult = validator.validate(validDto);

        assertThat(actualResult.hasErrors()).isFalse();

    }

    private Instant getExpirationDate(String stringDate, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault());
        LocalDateTime localDateTime = LocalDateTime.parse(stringDate, dateTimeFormatter);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return zonedDateTime.toInstant();
    }
}
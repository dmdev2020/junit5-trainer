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

    @Test
    void whenUserIdNull_thenValidationFails() {
        CreateSubscriptionDto invalidDto = CreateSubscriptionDto.builder()
                .name("Test Name")
                .provider(Provider.APPLE.name())
                .expirationDate(getExpirationDate("21:00:00, 30.04.2024", "H:m:s, d.M.y"))
                .build();

        ValidationResult actualResult = validator.validate(invalidDto);

        assertThat(actualResult.hasErrors()).isTrue();
        assertThat(actualResult.getErrors().size()).isEqualTo(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(100);
        assertThat(actualResult.getErrors().get(0).getMessage()).isEqualTo("userId is invalid");
    }

    @Test
    void whenNameIsBlank_thenValidationFails() {
        CreateSubscriptionDto invalidDto = CreateSubscriptionDto.builder()
                .userId(1)
                .provider(Provider.APPLE.name())
                .expirationDate(getExpirationDate("21:00:00, 30.04.2024", "H:m:s, d.M.y"))
                .build();

        ValidationResult actualResult = validator.validate(invalidDto);

        assertThat(actualResult.hasErrors()).isTrue();
        assertThat(actualResult.getErrors().size()).isEqualTo(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(101);
        assertThat(actualResult.getErrors().get(0).getMessage()).isEqualTo("name is invalid");
    }

    @Test
    void whenProviderIsEmpty_thenValidationFails() {
        CreateSubscriptionDto invalidDto = CreateSubscriptionDto.builder()
                .userId(1)
                .name("Test Name")
                .expirationDate(getExpirationDate("21:00:00, 30.04.2024", "H:m:s, d.M.y"))
                .build();

        ValidationResult actualResult = validator.validate(invalidDto);

        assertThat(actualResult.hasErrors()).isTrue();
        assertThat(actualResult.getErrors().size()).isEqualTo(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(102);
        assertThat(actualResult.getErrors().get(0).getMessage()).isEqualTo("provider is invalid");
    }

    @Test
    void whenExpirationDateIsAbsent_thenValidationFails() {
        CreateSubscriptionDto invalidDto = CreateSubscriptionDto.builder()
                .userId(1)
                .name("Test Name")
                .provider(Provider.APPLE.name())
                .build();

        ValidationResult actualResult = validator.validate(invalidDto);

        assertThat(actualResult.hasErrors()).isTrue();
        assertThat(actualResult.getErrors().size()).isEqualTo(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(103);
        assertThat(actualResult.getErrors().get(0).getMessage()).isEqualTo("expirationDate is invalid");
    }

    @Test
    void whenExpirationDateIsInPast_thenValidationFails() {
        CreateSubscriptionDto invalidDto = CreateSubscriptionDto.builder()
                .userId(1)
                .name("Test Name")
                .provider(Provider.APPLE.name())
                .expirationDate(getExpirationDate("21:00:00, 30.04.2023", "H:m:s, d.M.y"))
                .build();

        ValidationResult actualResult = validator.validate(invalidDto);

        assertThat(actualResult.hasErrors()).isTrue();
        assertThat(actualResult.getErrors().size()).isEqualTo(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(103);
        assertThat(actualResult.getErrors().get(0).getMessage()).isEqualTo("expirationDate is invalid");
    }

    private Instant getExpirationDate(String stringDate, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault());
        LocalDateTime localDateTime = LocalDateTime.parse(stringDate, dateTimeFormatter);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return zonedDateTime.toInstant();
    }
}
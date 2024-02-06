package com.dmdev.validator;

import com.dmdev.dto.CreateSubscriptionDto;
import com.dmdev.entity.Provider;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static com.dmdev.util.DateUtil.getExpirationDate;
import static org.assertj.core.api.Assertions.assertThat;

class CreateSubscriptionValidatorTest {

    private final CreateSubscriptionValidator validator = CreateSubscriptionValidator.getInstance();

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

    @Test
    void whenAllCheckPointsFail_thenValidationFails() {
        CreateSubscriptionDto invalidDto = CreateSubscriptionDto.builder()
                .expirationDate(getExpirationDate("21:00:00, 30.04.2023", "H:m:s, d.M.y"))
                .build();

        ValidationResult actualResult = validator.validate(invalidDto);

        assertThat(actualResult.hasErrors()).isTrue();
        assertThat(actualResult.getErrors().size()).isEqualTo(4);
        assertThat(actualResult.getErrors().stream().map(Error::getCode).collect(Collectors.toList()))
                .containsExactly(100, 101, 102, 103);
        assertThat(actualResult.getErrors().stream().map(Error::getMessage).collect(Collectors.toList()))
                .containsExactly("userId is invalid", "name is invalid", "provider is invalid", "expirationDate is invalid");
    }

}
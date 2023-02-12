package com.dmdev.validator;

import com.dmdev.dto.CreateSubscriptionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

import static com.dmdev.entity.Provider.GOOGLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateSubscriptionValidatorTest {

    private final CreateSubscriptionValidator validator = CreateSubscriptionValidator.getInstance();

    @Test
    void shouldPassValidation_whenAllDataIsValidTest() {
        //given
        var dto = CreateSubscriptionDto.builder()
                .name("Ivan")
                .userId(1)
                .provider(GOOGLE.name())
                .expirationDate(Instant.now().plus(Duration.ofDays(2)))
                .build();
        //when
        var actualResult = validator.validate(dto);
        //then
        assertThat(actualResult.hasErrors()).isFalse();
    }

    @Test
    void shouldFailValidation_whenAllDataIsInvalidTest() {
        //given
        var dto = CreateSubscriptionDto.builder()
                .name("")
                .userId(null)
                .provider("dummy")
                .expirationDate(Instant.now().minus(Duration.ofDays(2)))
                .build();
        //when
        var actualResult = validator.validate(dto);
        //then
        assertThat(actualResult.hasErrors()).isTrue();
        assertThat(actualResult.getErrors()).hasSize(4);
        var errorCodes = actualResult.getErrors().stream()
                .map(Error::getCode)
                .toList();
        assertThat(errorCodes).contains(100, 101, 102, 103);
    }

    @ParameterizedTest
    @MethodSource("getValidationErrorArguments")
    void checkEachValidationErrorTest(String name, Integer id, String provider, Instant expirationDate,
                                      String expectedResult
    ) {
        //given
        var dto = CreateSubscriptionDto.builder()
                .name(name)
                .userId(id)
                .provider(provider)
                .expirationDate(expirationDate)
                .build();
        //given
        var actualResult = validator.validate(dto);
        //then
        assertTrue(actualResult.hasErrors());
        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getMessage()).isEqualTo(expectedResult);
    }

    static Stream<Arguments> getValidationErrorArguments() {
        return Stream.of(
                Arguments.of("", 1, GOOGLE.name(), Instant.now().plus(Duration.ofDays(1)),
                        "name is invalid"),
                Arguments.of("Ivan", 1, "fake", Instant.now().plus(Duration.ofDays(1)),
                        "provider is invalid"),
                Arguments.of("Ivan", 1, GOOGLE.name(), Instant.now().minus(Duration.ofDays(1)),
                        "expirationDate is invalid"),
                Arguments.of("Ivan", null, GOOGLE.name(), Instant.now().plus(Duration.ofDays(1)),
                        "userId is invalid")
        );
    }
}
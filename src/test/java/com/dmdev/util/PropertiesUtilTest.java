package com.dmdev.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class PropertiesUtilTest {

    public static Stream<Arguments> propertiesProvider() {
        return Stream.of(
                arguments("db.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"),
                arguments("db.user", "sa"),
                arguments("db.driver", "org.h2.Driver")
        );
    }

    @ParameterizedTest
    @MethodSource("propertiesProvider")
    void whenPropertiesProvided_thenPropertiesSet(String key, String expectedValue) {
        String actualValue = PropertiesUtil.get(key);
        assertThat(actualValue).isEqualTo(expectedValue);
    }

}
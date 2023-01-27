package com.dmdev.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Gender {
    MALE,
    FEMALE;

    public static Gender find(String gender) {
        return findOpt(gender).orElseThrow();
    }

    public static Optional<Gender> findOpt(String gender) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(gender))
                .findFirst();
    }
}

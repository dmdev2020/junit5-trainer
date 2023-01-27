package com.dmdev.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Role {
    USER,
    ADMIN;

    public static Role find(String role) {
        return findOpt(role).orElseThrow();
    }

    public static Optional<Role> findOpt(String role) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(role))
                .findFirst();
    }
}

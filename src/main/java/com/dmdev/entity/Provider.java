package com.dmdev.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Provider {
    GOOGLE, APPLE;

    public static Provider findByName(String name) {
        return findByNameOpt(name).orElseThrow();
    }

    public static Optional<Provider> findByNameOpt(String name) {
        return Arrays.stream(values())
                .filter(provider -> provider.name().equalsIgnoreCase(name))
                .findFirst();
    }
}

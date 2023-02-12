package com.dmdev.integration.utils;

import com.dmdev.dto.CreateSubscriptionDto;
import com.dmdev.entity.Subscription;
import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.Instant;

import static com.dmdev.entity.Provider.APPLE;
import static com.dmdev.entity.Provider.GOOGLE;
import static com.dmdev.entity.Status.ACTIVE;

@UtilityClass
public class TestUtils {

    public final Subscription IVAN = Subscription.builder()
            .userId(1)
            .name("Ivan")
            .provider(GOOGLE)
            .expirationDate(Instant.parse("2025-01-12T18:58:54.411928400Z"))
            .status(ACTIVE)
            .build();

    public final Subscription NEW_SUBSCRIPTION = Subscription.builder()
            .userId(4)
            .name("Max")
            .provider(GOOGLE)
            .expirationDate(Instant.now().plus(Duration.ofDays(2)))
            .status(ACTIVE)
            .build();

    public final CreateSubscriptionDto IVAN_DTO = CreateSubscriptionDto.builder()
            .userId(1)
            .name("Ivan")
            .provider(GOOGLE.name())
            .expirationDate(Instant.parse("2025-01-12T18:58:54.411928400Z"))
            .build();
    public final CreateSubscriptionDto DTO = CreateSubscriptionDto.builder()
            .userId(15)
            .name("Dima")
            .provider(APPLE.name())
            .expirationDate(Instant.now().plus(Duration.ofDays(2)))
            .build();
}

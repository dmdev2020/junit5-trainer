package com.dmdev.mapper;

import com.dmdev.dto.CreateSubscriptionDto;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static com.dmdev.entity.Provider.GOOGLE;
import static com.dmdev.entity.Status.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;

class CreateSubscriptionMapperTest {

    private final CreateSubscriptionMapper mapper = CreateSubscriptionMapper.getInstance();

    @Test
    void shouldMapping_whenCreateSubscriptionDtoIsValidTest() {
        //given
        var dto = CreateSubscriptionDto.builder()
                .userId(1)
                .name("Ivan")
                .provider(GOOGLE.name())
                .expirationDate(Instant.now().plus(Duration.ofDays(2)))
                .build();
        //when
        var actualResult = mapper.map(dto);
        //then
        assertThat(actualResult.getUserId()).isEqualTo(dto.getUserId());
        assertThat(actualResult.getName()).isEqualTo(dto.getName());
        assertThat(actualResult.getProvider().name()).isEqualTo(dto.getProvider());
        assertThat(actualResult.getExpirationDate()).isEqualTo(dto.getExpirationDate());
        assertThat(actualResult.getStatus()).isEqualTo(ACTIVE);
    }

}
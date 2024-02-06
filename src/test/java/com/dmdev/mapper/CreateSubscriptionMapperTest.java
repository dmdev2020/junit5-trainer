package com.dmdev.mapper;

import com.dmdev.dto.CreateSubscriptionDto;
import com.dmdev.entity.Provider;
import com.dmdev.entity.Status;
import com.dmdev.entity.Subscription;
import org.junit.jupiter.api.Test;

import static com.dmdev.util.DateUtil.getExpirationDate;
import static org.assertj.core.api.Assertions.assertThat;

class CreateSubscriptionMapperTest {

    private final CreateSubscriptionMapper mapper = CreateSubscriptionMapper.getInstance();

    @Test
    void whenValidDto_thenValidSubscriptionCreated() {
        String date = "21:00:00, 30.04.2024";
        String datePattern = "H:m:s, d.M.y";
        CreateSubscriptionDto validDto = CreateSubscriptionDto.builder()
                .userId(1)
                .name("Test Name")
                .provider(Provider.GOOGLE.name())
                .expirationDate(getExpirationDate(date, datePattern))
                .build();

        Subscription actualSub = mapper.map(validDto);
        Subscription expectedSub = Subscription.builder()
                .userId(1)
                .name("Test Name")
                .provider(Provider.GOOGLE)
                .expirationDate(getExpirationDate(date, datePattern))
                .status(Status.ACTIVE)
                .build();

        assertThat(actualSub).isEqualTo(expectedSub);

    }
}
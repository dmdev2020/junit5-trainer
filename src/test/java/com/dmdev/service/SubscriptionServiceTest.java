package com.dmdev.service;

import com.dmdev.dao.SubscriptionDao;
import com.dmdev.dto.CreateSubscriptionDto;
import com.dmdev.entity.Provider;
import com.dmdev.entity.Status;
import com.dmdev.entity.Subscription;
import com.dmdev.mapper.CreateSubscriptionMapper;
import com.dmdev.validator.CreateSubscriptionValidator;
import com.dmdev.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Optional;

import static com.dmdev.util.DateUtil.getExpirationDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private SubscriptionDao subDao;
    @Mock
    private CreateSubscriptionMapper createSubMapper;
    @Mock
    private CreateSubscriptionValidator createSubValidator;
    @Mock
    private Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

    @InjectMocks
    private SubscriptionService subService; // );

    @Test
    void whenSubscriptionExists_thenUpdateAndInsert() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(23)
                .name("Test Name")
                .provider(Provider.APPLE.name())
                .expirationDate(getExpirationDate("21:00:00, 30.04.2024", "H:m:s, d.M.y"))
                .build();

        Subscription existingSub = Subscription.builder()
                .userId(23)
                .name("Test Name")
                .provider(Provider.APPLE)
                .expirationDate(getExpirationDate("23:59:59, 31.03.2024", "H:m:s, d.M.y"))
                .build();

        Subscription updatedSub = Subscription.builder()
                .userId(23)
                .name("Test Name")
                .provider(Provider.APPLE)
                .expirationDate(dto.getExpirationDate())
                .status(Status.ACTIVE)
                .build();

        doReturn(new ValidationResult()).when(createSubValidator).validate(dto);
        doReturn(Collections.singletonList(existingSub)).when(subDao).findByUserId(dto.getUserId());

        // Ask stub DAO.upsert() method to return whichever argument it receives
        when(subDao.upsert(any(Subscription.class))).then(AdditionalAnswers.returnsFirstArg());

        assertThat(subService.upsert(dto)).isEqualTo(updatedSub);
        verify(subDao).upsert(existingSub);

    }

    @Test
    void whenActiveSubscriptionExists_thenCancelIt() {
        Subscription existingSub = Subscription.builder()
                .id(243)
                .userId(23)
                .name("Test Name")
                .provider(Provider.APPLE)
                .expirationDate(getExpirationDate("23:59:59, 31.03.2024", "H:m:s, d.M.y"))
                .status(Status.ACTIVE)
                .build();

        doReturn(Optional.of(existingSub)).when(subDao).findById(any());
        // Ask stub DAO.update() method to return whichever argument it receives
        when(subDao.update(any(Subscription.class))).then(AdditionalAnswers.returnsFirstArg());

        subService.cancel(existingSub.getId());

        assertThat(existingSub.getStatus()).isEqualTo(Status.CANCELED);
        verify(subDao).update(existingSub);
    }

    @Test
    void whenNonExpiredSubscriptionExists_thenExpireIt() {
        Subscription existingSub = Subscription.builder()
                .id(243)
                .userId(23)
                .name("Test Name")
                .provider(Provider.APPLE)
                .expirationDate(getExpirationDate("23:59:59, 31.03.2024", "H:m:s, d.M.y"))
                .status(Status.ACTIVE)
                .build();

        doReturn(Optional.of(existingSub)).when(subDao).findById(any());
        // Ask stub DAO.update() method to return whichever argument it receives
        when(subDao.update(any(Subscription.class))).then(AdditionalAnswers.returnsFirstArg());

        subService.expire(existingSub.getId());

        assertThat(existingSub.getStatus()).isEqualTo(Status.EXPIRED);
        assertThat(existingSub.getExpirationDate()).isEqualTo(Instant.now(clock));
        verify(subDao).update(existingSub);
    }
}
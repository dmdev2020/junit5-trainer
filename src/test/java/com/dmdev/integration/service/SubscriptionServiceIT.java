package com.dmdev.integration.service;

import com.dmdev.dao.SubscriptionDao;
import com.dmdev.integration.IntegrationTestBase;
import com.dmdev.integration.utils.TestUtils;
import com.dmdev.mapper.CreateSubscriptionMapper;
import com.dmdev.service.SubscriptionService;
import com.dmdev.validator.CreateSubscriptionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;

import static com.dmdev.entity.Status.ACTIVE;
import static com.dmdev.entity.Status.CANCELED;
import static com.dmdev.entity.Status.EXPIRED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;


class SubscriptionServiceIT extends IntegrationTestBase {

    private SubscriptionDao subscriptionDao;
    private SubscriptionService service;
    private CreateSubscriptionMapper mapper;


    @BeforeEach
    void init() {
        service = new SubscriptionService(
                subscriptionDao = spy(SubscriptionDao.getInstance()),
                mapper = CreateSubscriptionMapper.getInstance(),
                CreateSubscriptionValidator.getInstance(),
                Clock.systemDefaultZone()
        );

    }

    @Test
    void shouldUpsetSuccessfully_whenSubscriptionExistIT() {
        var subscriptions = subscriptionDao.findByUserId(TestUtils.IVAN.getUserId());
        var subscription = subscriptions.get(0);
        subscription.setExpirationDate(TestUtils.IVAN_DTO.getExpirationDate());
        //when
        var actualResult = service.upsert(TestUtils.IVAN_DTO);
        //then
        assertAll(
                () -> assertThat(actualResult.getId()).isNotNull(),
                () -> assertThat(actualResult.getUserId()).isEqualTo(TestUtils.IVAN_DTO.getUserId()),
                () -> assertThat(actualResult.getName()).isEqualTo(TestUtils.IVAN_DTO.getName()),
                () -> assertThat(actualResult.getProvider().name()).isEqualTo(TestUtils.IVAN_DTO.getProvider()),
                () -> assertThat(actualResult.getExpirationDate()).isEqualTo(TestUtils.IVAN_DTO.getExpirationDate()),
                () -> assertThat(actualResult.getStatus()).isEqualTo(ACTIVE),
                () -> verify(subscriptionDao).update(subscription),
                () -> verify(subscriptionDao, never()).insert(subscription)
        );
    }

    @Test
    void shouldUpsetSuccessfully_whenSubscriptionNotExistIT() {
        var subscription = mapper.map(TestUtils.DTO);
        //when
        var actualResult = service.upsert(TestUtils.DTO);
        //then
        assertAll(
                () -> assertThat(actualResult.getId()).isNotNull(),
                () -> assertThat(actualResult.getUserId()).isEqualTo(TestUtils.DTO.getUserId()),
                () -> assertThat(actualResult.getName()).isEqualTo(TestUtils.DTO.getName()),
                () -> assertThat(actualResult.getProvider().name()).isEqualTo(TestUtils.DTO.getProvider()),
                () -> assertThat(actualResult.getExpirationDate()).isEqualTo(TestUtils.DTO.getExpirationDate()),
                () -> assertThat(actualResult.getStatus()).isEqualTo(ACTIVE),
                () -> verify(subscriptionDao, never()).update(subscription)
        );
    }

    @Test
    void shouldCancelSuccessfully() {
        var subscriptions = subscriptionDao.findByUserId(TestUtils.IVAN.getUserId());
        var subscription = subscriptions.get(0);
        //when
        service.cancel(subscription.getId());
        //then
        subscription.setStatus(CANCELED);
        assertAll(
                () -> verify(subscriptionDao).update(subscription),
                () -> assertThat(subscriptionDao.findById(subscription.getId()).get().getStatus()).isEqualTo(CANCELED)
        );
    }

    @Test
    void shouldExpireSuccessfully() {
        var subscriptions = subscriptionDao.findByUserId(TestUtils.IVAN.getUserId());
        var subscription = subscriptions.get(0);
        //when
        service.expire(subscription.getId());
        //then
        assertThat(subscriptionDao.findById(subscription.getId()).get().getStatus()).isEqualTo(EXPIRED);
    }

}
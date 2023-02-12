package com.dmdev.service;

import com.dmdev.dao.SubscriptionDao;
import com.dmdev.dto.CreateSubscriptionDto;
import com.dmdev.entity.Subscription;
import com.dmdev.exception.SubscriptionException;
import com.dmdev.exception.ValidationException;
import com.dmdev.mapper.CreateSubscriptionMapper;
import com.dmdev.validator.CreateSubscriptionValidator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.dmdev.entity.Provider.GOOGLE;
import static com.dmdev.entity.Status.ACTIVE;
import static com.dmdev.entity.Status.CANCELED;
import static com.dmdev.entity.Status.EXPIRED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    private static final Integer ID = 2;
    private static final Integer USER_ID = 1;
    private static final String USER_NAME = "Ivan";
    private static final Instant EXPIRATION_DATE = Instant.now().plus(Duration.ofDays(1));

    @Captor
    private ArgumentCaptor<Subscription> subscriptionArgumentCaptor;
    @Mock
    private SubscriptionDao subscriptionDao;
    @Spy
    private CreateSubscriptionMapper createSubscriptionMapper;
    @Spy
    private CreateSubscriptionValidator createSubscriptionValidator;
    @Mock
    private Clock clock;
    @InjectMocks
    private SubscriptionService service;

    @Nested
    @Tag("upsert")
    class UpsertTest {

        @Test
        void shouldUpdateSubscription_whenSubscriptionExistInDBTest() {
            //given
            var dto = createCreateSubscriptionDto();
            var subscriptions = List.of(createSubscription());
            doReturn(subscriptions).when(subscriptionDao).findByUserId(dto.getUserId());
            doReturn(subscriptions.get(0)).when(subscriptionDao).upsert(subscriptions.get(0));
            //when
            var actualResult = service.upsert(dto);
            //then
            verify(createSubscriptionValidator).validate(dto);
            verify(subscriptionDao).findByUserId(dto.getUserId());
            verify(subscriptionDao).upsert(subscriptions.get(0));
            assertThat(actualResult).isEqualTo(subscriptions.get(0));
        }

        @Test
        void shouldCreateSubscription_whenSubscriptionNotExistInDBTest() {
            //given
            var dto = createCreateSubscriptionDto();
            var subscription = createSubscription();
            subscription.setId(null);
            var subscriptions = List.of(subscription);
            doReturn(Collections.emptyList()).when(subscriptionDao).findByUserId(dto.getUserId());
            doReturn(subscriptions.get(0)).when(subscriptionDao).upsert(subscriptions.get(0));
            //when
            var actualResult = service.upsert(dto);
            //then
            verify(createSubscriptionValidator).validate(dto);
            verify(subscriptionDao).findByUserId(dto.getUserId());
            verify(subscriptionDao).upsert(subscription);
            assertThat(actualResult).isEqualTo(subscription);
        }

        @Test
        void shouldTrowValidationException_whenGotInvalidArgument() {
            //given
            var dto = CreateSubscriptionDto.builder()
                    .userId(null)
                    .name(USER_NAME)
                    .provider(GOOGLE.name())
                    .expirationDate(EXPIRATION_DATE)
                    .build();
            //when
            //then
            var exception = assertThrows(ValidationException.class, () -> service.upsert(dto));
            assertThat(exception.getErrors().get(0).getMessage()).isEqualTo("userId is invalid");
        }

    }

    @Nested
    @Tag("cancel")
    class CancelTest {

        @Test
        void shouldInvokeUpdate_whenSubscriptionExistInDBTest() {
            //given
            var subscription = createSubscription();
            doReturn(Optional.of(subscription)).when(subscriptionDao).findById(subscription.getId());
            //when
            service.cancel(subscription.getId());
            //then
            verify(subscriptionDao).findById(subscription.getId());
            verify(subscriptionDao).update(subscriptionArgumentCaptor.capture());
            assertThat(subscriptionArgumentCaptor.getValue().getStatus()).isEqualTo(CANCELED);
        }

        @Test
        void shouldThrowIllegalArgumentException_whenSubscriptionNotExistInDBTest() {
            //given
            doReturn(Optional.empty()).when(subscriptionDao).findById(anyInt());
            //when
            //then
            assertThrows(IllegalArgumentException.class, () -> service.cancel(anyInt()));
        }

        @Test
        void shouldThrowSubscriptionException_whenSubscriptionStatusNotEqActiveTest() {
            //given
            var subscription = createSubscription();
            subscription.setStatus(EXPIRED);
            doReturn(Optional.of(subscription)).when(subscriptionDao).findById(subscription.getId());
            //when
            //then
            var exception = assertThrows(SubscriptionException.class,
                    () -> service.cancel(subscription.getId()));
            assertThat(exception.getMessage()).isEqualTo("Only active subscription 2 can be canceled");
        }

    }

    @Nested
    @Tag("expire")
    class ExpireTest {

        @Test
        void shouldInvokeUpdate_whenSubscriptionExistInDBTest() {
            //given
            var subscription = createSubscription();
            doReturn(Optional.of(subscription)).when(subscriptionDao).findById(subscription.getId());
            //when
            service.expire(subscription.getId());
            //then
            verify(subscriptionDao).findById(subscription.getId());
            verify(subscriptionDao).update(subscriptionArgumentCaptor.capture());
            assertThat(subscriptionArgumentCaptor.getValue().getStatus()).isEqualTo(EXPIRED);
        }

        @Test
        void shouldThrowIllegalArgumentException_whenSubscriptionNotExistInDBTest() {
            //given
            doReturn(Optional.empty()).when(subscriptionDao).findById(anyInt());
            //when
            //then
            assertThrows(IllegalArgumentException.class, () -> service.expire(anyInt()));
        }

        @Test
        void shouldThrowSubscriptionException_whenSubscriptionStatusEqExpiredTest() {
            //given
            var subscription = createSubscription();
            subscription.setStatus(EXPIRED);
            doReturn(Optional.of(subscription)).when(subscriptionDao).findById(subscription.getId());
            //when
            //then
            var subscriptionException = assertThrows(SubscriptionException.class,
                    () -> service.expire(subscription.getId()));
            assertThat(subscriptionException.getMessage()).isEqualTo("Subscription 2 has already expired");
        }

    }

    private static Subscription createSubscription() {
        return new Subscription(ID, USER_ID, USER_NAME, GOOGLE, EXPIRATION_DATE, ACTIVE);
    }

    private static CreateSubscriptionDto createCreateSubscriptionDto() {
        return CreateSubscriptionDto.builder()
                .userId(USER_ID)
                .name(USER_NAME)
                .provider(GOOGLE.name())
                .expirationDate(EXPIRATION_DATE)
                .build();
    }

}
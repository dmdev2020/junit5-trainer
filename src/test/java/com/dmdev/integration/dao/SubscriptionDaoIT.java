package com.dmdev.integration.dao;

import com.dmdev.dao.SubscriptionDao;
import com.dmdev.integration.IntegrationTestBase;
import com.dmdev.integration.utils.TestUtils;
import org.junit.jupiter.api.Test;

import static com.dmdev.entity.Provider.APPLE;
import static com.dmdev.entity.Provider.GOOGLE;
import static org.assertj.core.api.Assertions.assertThat;

class SubscriptionDaoIT extends IntegrationTestBase {

    private final SubscriptionDao subscriptionDao = SubscriptionDao.getInstance();

    @Test
    void shouldFindAllIT() {
        assertThat(subscriptionDao.findAll()).hasSize(3);
    }

    @Test
    void shouldFindByIdIT() {
        //given
        var subscriptions = subscriptionDao.findAll();
        //when
        var actualResult = subscriptionDao.findById(subscriptions.get(0).getId());
        //then
        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getName()).isEqualTo(TestUtils.IVAN.getName());
    }

    @Test
    void shouldNotFindNotExistingSubscriptionByIdIT() {
        //when
        var actualResult = subscriptionDao.findById(32);
        //then
        assertThat(actualResult).isNotPresent();
    }

    @Test
    void shouldDeleteIT() {
        //given
        var subscriptions = subscriptionDao.findAll();
        assertThat(subscriptions).hasSize(3);
        //then
        assertThat(subscriptionDao.delete(subscriptions.get(1).getId())).isTrue();
        assertThat(subscriptionDao.findAll()).hasSize(2);
    }

    @Test
    void shouldNotDeleteNotExistingSubscriptionIT() {
        assertThat(subscriptionDao.delete(32)).isFalse();
    }

    @Test
    void shouldUpdateIT() {
        //given
        var subscriptions = subscriptionDao.findByUserId(TestUtils.IVAN.getUserId());
        var ivan = subscriptions.get(0);
        assertThat(ivan.getProvider()).isEqualTo(GOOGLE);
        ivan.setProvider(APPLE);
        //when
        subscriptionDao.update(ivan);
        //then
        var actualResult = subscriptionDao.findByUserId(ivan.getUserId());
        assertThat(actualResult.get(0).getProvider()).isEqualTo(APPLE);
    }

    @Test
    void shouldInsertIT() {
        //given
        assertThat(TestUtils.NEW_SUBSCRIPTION.getId()).isNull();
        assertThat(subscriptionDao.findAll()).hasSize(3);
        //when
        var actualResult = subscriptionDao.insert(TestUtils.NEW_SUBSCRIPTION);
        //then
        assertThat(actualResult.getId()).isNotNull();
        assertThat(actualResult.getUserId()).isEqualTo(TestUtils.NEW_SUBSCRIPTION.getUserId());
        assertThat(subscriptionDao.findAll()).hasSize(4);
    }

    @Test
    void shouldFindByUserIdIT() {
        //when
        var actualResult = subscriptionDao.findByUserId(TestUtils.IVAN.getUserId());
        //then
        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult.get(0).getUserId()).isEqualTo(TestUtils.IVAN.getUserId());
    }

    @Test
    void shouldNotFindNotExistingSubscriptionByUserIdIT() {
        //when
        var actualResult = subscriptionDao.findByUserId(32);
        //then
        assertThat(actualResult).isEmpty();
    }

}
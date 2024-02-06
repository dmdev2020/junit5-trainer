package com.dmdev.dao;

import com.dmdev.entity.Provider;
import com.dmdev.entity.Status;
import com.dmdev.entity.Subscription;
import com.dmdev.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.dmdev.util.DateUtil.getExpirationDate;
import static org.assertj.core.api.Assertions.assertThat;

class SubscriptionDaoIT extends IntegrationTestBase {

    private final SubscriptionDao subDao = SubscriptionDao.getInstance();

    @Test
    void whenFindAll_thenAllSubsFound() {
        Subscription sub1 = subDao.insert(getSubscription("User 1", 1));
        Subscription sub2 = subDao.insert(getSubscription("User 2", 2));
        Subscription sub3 = subDao.insert(getSubscription("User 3", 3));

        List<Subscription> actualSub = subDao.findAll();
        List<Integer> userIds = actualSub.stream().map(Subscription::getUserId).toList();

        assertThat(userIds).containsExactly(sub1.getUserId(), sub2.getUserId(), sub3.getUserId());
    }

    @Test
    void whenSubExists_thenItIsFindById() {
        Subscription expectedSub = subDao.insert(getSubscription("User 1", 1));

        Optional<Subscription> actualSub = subDao.findById(expectedSub.getId());

        assertThat(actualSub).isPresent();
        assertThat(actualSub.get()).isEqualTo(expectedSub);
    }

    @Test
    void whenSubExists_thenItIsDeleted() {
        Subscription savedSub = subDao.insert(getSubscription("User 1", 1));
        boolean isDeleted = subDao.delete(savedSub.getId());

        assertThat(isDeleted).isTrue();
    }

    @Test
    void whenSubDoesNotExist_thenItIsNotDeleted() {
        subDao.insert(getSubscription("User 1", 1));
        boolean isDeleted = subDao.delete(11);

        assertThat(isDeleted).isFalse();
    }

    @Test
    void update() {
        Subscription insertedSub = subDao.insert(getSubscription("User 1", 1));
        insertedSub.setStatus(Status.CANCELED);
        insertedSub.setExpirationDate(getExpirationDate("13:50:00, 15.03.2024", "H:m:s, d.M.y"));
        subDao.update(insertedSub);

        Optional<Subscription> foundSub = subDao.findById(insertedSub.getId());

        assertThat(insertedSub).isEqualTo(foundSub.get());

    }

    @Test
    void insert() {
        Subscription expectedSub = getSubscription("User 1", 1);
        Subscription actualSub = subDao.insert(expectedSub);

        assertThat(actualSub.getId()).isNotNull();
    }

    @Test
    void findByUserId() {
        Subscription expectedSub = subDao.insert(getSubscription("User 1", 1));

        List<Subscription> actualSubs = subDao.findByUserId(expectedSub.getUserId());

        assertThat(actualSubs.size()).isEqualTo(1);
        assertThat(actualSubs).containsExactly(expectedSub);
    }

    private Subscription getSubscription(String userName, int userId) {
        return Subscription.builder()
                .userId(userId)
                .name(userName)
                .provider(Provider.APPLE)
                .expirationDate(getExpirationDate("23:59:59, 31.03.2024", "H:m:s, d.M.y"))
                .status(Status.ACTIVE)
                .build();
    }
}
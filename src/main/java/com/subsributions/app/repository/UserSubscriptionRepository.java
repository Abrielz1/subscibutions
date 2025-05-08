package com.subsributions.app.repository;

import com.subsributions.app.entity.UserSubscription;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    @EntityGraph(attributePaths = {"user", "subscription"})
    Optional<UserSubscription> findUserSubscriptionByUserIdAndSubscriptionId(Long userId, Long subscriptionId);
}

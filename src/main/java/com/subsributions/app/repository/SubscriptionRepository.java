package com.subsributions.app.repository;

import com.subsributions.app.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Boolean existsByName(String name);

    Optional<Subscription> findByName(String name);
}

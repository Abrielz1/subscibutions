package com.subsributions.app.repository;

import com.subsributions.app.entity.UserSubscription;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    /**
     * Находит связь пользователя и подписки по их ID.
     * Использует EntityGraph для загрузки связанных сущностей.
     */
    @EntityGraph(attributePaths = {"user", "subscription"})
    Optional<UserSubscription> findUserSubscriptionByUserIdAndSubscriptionId(Long userId, Long subscriptionId);

    /**
     * Возвращает топ-N популярных подписок.
     * - Группирует по ID и названию подписки.
     * - Считает активных пользователей (is_declined = false).
     * - Сортирует по убыванию количества пользователей.
     */
    @Query(value = """
         SELECT s.name,
                COUNT(us.user_id) AS count
           FROM subscriptions s
           INNER JOIN user_subscriptions us
               ON s.id = us.subscription_id
               AND us.is_declined = false
           GROUP BY s.id, s.name
           ORDER BY count DESC
           LIMIT :top
            """ ,nativeQuery = true)
    List<TopSubscriptionProjection> getAllActiveTop3Subscriptions(@Param("top")Integer top);
}

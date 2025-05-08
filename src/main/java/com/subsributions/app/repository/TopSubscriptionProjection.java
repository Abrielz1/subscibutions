package com.subsributions.app.repository;

/**
 * Проекция для передачи данных о популярных подписках.
 * - name: Название подписки.
 * - count: Количество активных пользователей.
 */
public interface TopSubscriptionProjection {
    String getName();
    Long getCount();
}

package com.subsributions.app.model.response.subscribution;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Краткая информация о подписке для топа.
 * - name: Название подписки.
 * - count: Количество активных пользователей.
 */
@Schema(description = "Статистика популярности подписок")
public record SubscriptionResponseShortestDto(@Schema(description = "Название подписки", example = "Netflix")
                                              String name,
                                              @Schema(description = "счётчик подписчиков")
                                              Long  count) {
}

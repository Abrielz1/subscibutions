package com.subsributions.app.model.response.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record SubscriptionResponseDto(@Schema(description = "название подписки")
                                      String name,

                                      @Schema(description = "дата начала подписки")
                                      LocalDate startOfSubscription,

                                      @Schema(description = "дата окончания подписки")
                                      LocalDate endOfSubscription) {
}

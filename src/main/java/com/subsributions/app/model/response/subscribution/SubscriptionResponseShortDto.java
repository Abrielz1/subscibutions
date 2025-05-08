package com.subsributions.app.model.response.subscribution;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Краткая информация о подписке")
public record SubscriptionResponseShortDto( @Schema(description = "Электронная почта", example = "user@example.com") String email,
                                            @Schema(description = "Название подписки", example = "Netflix")
                                           String name,
                                            @Schema(description = "Дата начала", example = "2024-01-01")
                                           LocalDate startDate,
                                            @Schema(description = "Дата окончания", example = "2024-12-31")
                                           LocalDate endDate) {
}

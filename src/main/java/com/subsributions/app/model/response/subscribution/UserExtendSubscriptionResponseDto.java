package com.subsributions.app.model.response.subscribution;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record UserExtendSubscriptionResponseDto(@Schema(description = "электронная почта подписчика")
                                                String email,

                                                @Schema(description = "дата окончания подписки")
                                                LocalDate endDate) {
}

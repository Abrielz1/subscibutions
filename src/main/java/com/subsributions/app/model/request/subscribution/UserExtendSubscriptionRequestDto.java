package com.subsributions.app.model.request.subscribution;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record UserExtendSubscriptionRequestDto(@Schema(description = "электронная почта подписчика")
                                               @NotBlank
                                               @Email
                                               String email,

                                               @Schema(description = "название подписки")
                                               @NotBlank
                                               String name,

                                               @Schema(description = "дата окончания подписки")
                                               LocalDate endDate) {
}

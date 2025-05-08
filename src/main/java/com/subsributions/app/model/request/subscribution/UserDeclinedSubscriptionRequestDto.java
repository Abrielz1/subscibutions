package com.subsributions.app.model.request.subscribution;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDeclinedSubscriptionRequestDto(@Schema(description = "Электронная почта подписчика")
                                               @NotBlank
                                               @Email
                                               @Size(min = 8, max = 32)
                                               String email,

                                               @Schema(description = "название подписки")
                                               @NotBlank
                                               String name) {
}

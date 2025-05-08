package com.subsributions.app.model.request.subscribution;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "Запрос на отмену подписки")
public record UserDeclinedSubscriptionRequestDto(@Schema(description = "Электронная почта пользователя",
                                                        example = "user@example.com",
                                                        requiredMode = REQUIRED)
                                                @NotBlank
                                                @Email
                                                @Size(min = 8, max = 32)
                                                String email,

                                                @Schema(description = "Название подписки",
                                                        example = "Premium Access",
                                                        requiredMode = REQUIRED)
                                                @NotBlank
                                                String name) {
}

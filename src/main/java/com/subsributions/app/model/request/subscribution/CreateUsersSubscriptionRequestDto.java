package com.subsributions.app.model.request.subscribution;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "Запрос на создание подписки")
public record CreateUsersSubscriptionRequestDto(@Schema(description = "Электронная почта пользователя",
                                                example = "user@example.com",
                                                requiredMode = REQUIRED,
                                                minLength = 8,
                                                maxLength = 32)
                                                @NotBlank
                                                @Email
                                                @Size(min = 8, max = 32)
                                                String email,

                                                @Schema(description = "название подписки",
                                                        example = "Premium Access",
                                                        requiredMode = REQUIRED)
                                                @NotBlank
                                                String name,

                                                @Schema(description = "дата начала подписки",
                                                        example = "2024-01-01",
                                                        requiredMode = REQUIRED)
                                                @FutureOrPresent
                                                LocalDate startDate,

                                                @Schema(description = "дата окончания подписки",
                                                        example = "2024-12-31",
                                                        requiredMode = REQUIRED)
                                                @FutureOrPresent
                                                LocalDate endDate) {
}

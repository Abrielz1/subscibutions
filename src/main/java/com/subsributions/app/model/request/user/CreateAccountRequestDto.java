package com.subsributions.app.model.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "Запрос на создание учетной записи")
public record CreateAccountRequestDto(
                                       @Schema(description = "Электронная почта пользователя",
                                               example = "user@example.com",
                                               requiredMode = REQUIRED,
                                               minLength = 8,
                                               maxLength = 324)
                                       @Email
                                       @NotBlank
                                       @Size(min = 8, max = 32)
                                       String email,

                                       @Schema(description = "Пароль пользователя",
                                              example = "SecurePass123!",
                                              requiredMode = REQUIRED,
                                              minLength = 8,
                                              maxLength = 32)
                                       @NotBlank
                                       @Size(min = 8, max = 32)
                                       String password) {
}

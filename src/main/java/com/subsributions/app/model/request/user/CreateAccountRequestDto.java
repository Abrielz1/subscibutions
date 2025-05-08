package com.subsributions.app.model.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAccountRequestDto(@Schema(description = "Email user/Почта юзера")
                                   @Email
                                   @NotBlank
                                   String email,

                                   @NotBlank
                                   @Size(min = 8, max = 32)
                                   @Schema(description = "Password/Пароль")
                                   String password) {
}

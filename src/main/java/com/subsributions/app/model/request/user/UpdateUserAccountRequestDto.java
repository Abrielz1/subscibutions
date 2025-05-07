package com.subsributions.app.model.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserAccountRequestDto(@Schema(description = "Password/Пароль")
                                          @NotBlank
                                          String password) {
}

package com.subsributions.app.model.response.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponseDto(@Schema(description = "Электронная почта пользователя",
                                      example = "user@example.com")
                              String email) {
}

package com.subsributions.app.util.exception.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ошибка: пользователь не найден")
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

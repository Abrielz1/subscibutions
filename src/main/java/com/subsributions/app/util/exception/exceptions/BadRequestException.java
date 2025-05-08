package com.subsributions.app.util.exception.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ошибка: не правильный запрос")
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}

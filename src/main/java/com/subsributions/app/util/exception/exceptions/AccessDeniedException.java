package com.subsributions.app.util.exception.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ошибка: доступ запрещён")
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}


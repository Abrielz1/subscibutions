package com.subsributions.app.util.exception.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ошибка: конфликт данных")
public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException(String message) {
        super(message);
    }
}

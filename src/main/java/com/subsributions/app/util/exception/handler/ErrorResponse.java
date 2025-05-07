package com.subsributions.app.util.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
//@AllArgsConstructor
public class ErrorResponse {

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }

    private String error;

    private String description;
}

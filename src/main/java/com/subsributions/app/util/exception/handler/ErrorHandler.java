package com.subsributions.app.util.exception.handler;

import com.subsributions.app.util.exception.exceptions.AlreadyExistsException;
import com.subsributions.app.util.exception.exceptions.BadRequestException;
import com.subsributions.app.util.exception.exceptions.UserNotFoundException;
import com.subsributions.app.util.exception.exceptions.AccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerNotFoundException(final UserNotFoundException e) {

        log.warn("404 {}", e.getMessage(), e);
        return new ErrorResponse("User was not found 404", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerBadRequest(final BadRequestException e) {

        log.warn("400 {}", e.getMessage(), e);
        return new ErrorResponse("Bad request was committed 400 ", e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handlerUnsupportedState(final AccessDeniedException exception) {

        log.warn("403 {}", exception.getMessage(), exception);
        return new ErrorResponse(exception.getMessage(), exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handlerAlreadyExistsHandler(final AlreadyExistsException exception) {

        log.warn("401 {}", exception.getMessage(), exception);
        return new ErrorResponse("User already exists! 401 "
                + exception.getMessage(), exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerError(Exception ex) {
        log.error("Internal server error: {}", ex.getMessage(), ex);
        return new ErrorResponse("Internal server error", ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("Conflict: {}", ex.getMessage());
        return new ErrorResponse("Data conflict", ex.getMostSpecificCause().getMessage());
    }
}


package com.training.urlshortener.advice;

import com.training.urlshortener.dto.ApiErrorResponse;
import com.training.urlshortener.exception.MissingEntryException;
import com.training.urlshortener.factory.ErrorMessageFactory;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            HandlerMethodValidationException.class,
    })
    ResponseEntity<ApiErrorResponse> handleBadRequest(Exception ex, HttpServletRequest request) {
        return ErrorMessageFactory.getApiErrorResponseEntity(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(MissingEntryException.class)
    ResponseEntity<ApiErrorResponse> handleMissingEntryException(MissingEntryException ex, HttpServletRequest request) {
        return ErrorMessageFactory.getApiErrorResponseEntity(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiErrorResponse> handleGenericExceptions(Exception ex, HttpServletRequest request) {
        return ErrorMessageFactory.getApiErrorResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}

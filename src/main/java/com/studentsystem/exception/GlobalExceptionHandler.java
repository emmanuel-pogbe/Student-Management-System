package com.studentsystem.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.studentsystem.dto.response.ErrorResponse;
import com.studentsystem.exception.custom.EmailAlreadyInUseException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyInUse(EmailAlreadyInUseException ex, HttpServletRequest request) {
        logger.error("Email already in use: {}", request.getRequestURI(), ex);
        return buildErrorResponse(
            "USER_CREATION_ERROR",
            "That email is already in use",
            HttpStatus.CONFLICT,
            request
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllErrors(Exception ex, HttpServletRequest request) {
        logger.error("Unhandled exception while processing request: {}", request.getRequestURI(), ex);
        return buildErrorResponse(
            "INTERNAL_SERVER_ERROR",
            "We don't know what happened",
            HttpStatus.INTERNAL_SERVER_ERROR,
            request
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
        String errorCode,
        String message,
        HttpStatus status,
        HttpServletRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message, status.value(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, status);
    }
}

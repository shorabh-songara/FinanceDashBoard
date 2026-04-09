package com.Zorvyn.FinanceApp.exception;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.Zorvyn.FinanceApp.dto.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobleExceptionHandler extends RuntimeException{

    private static final Logger logger = LoggerFactory.getLogger(GlobleExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> HandleBadRequestException(Exception ex , HttpServletRequest request){

        ErrorResponse error = new ErrorResponse(false 
            , "Bad Request" 
            , ex.getMessage()
            , HttpStatus.BAD_REQUEST.value() 
            , request.getRequestURI() , LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> HandleResourceNotFoundException(Exception ex , HttpServletRequest request){

        ErrorResponse error = new ErrorResponse(false 
            , "Bad Request" 
            , ex.getMessage()
            , HttpStatus.NOT_FOUND.value() 
            , request.getRequestURI() , LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex, HttpServletRequest request) {

        logger.warn("Access denied at {}: {}", request.getRequestURI(), ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                false,
                "Forbidden",
                ex.getMessage(),
                403,
                request.getRequestURI(),
                LocalDateTime.now()
            );

        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(error);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {

        logger.warn("Illegal argument at {}: {}", request.getRequestURI(), ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                false,
                "Bad Request",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI(),
                LocalDateTime.now()
            );

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

}

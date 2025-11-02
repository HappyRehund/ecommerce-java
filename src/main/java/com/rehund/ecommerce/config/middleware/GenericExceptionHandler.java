package com.rehund.ecommerce.config.middleware;

import com.rehund.ecommerce.common.errors.BadRequestException;
import com.rehund.ecommerce.common.errors.ResourceNotFoundException;
import com.rehund.ecommerce.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// handle semua error yang terjadi di springboot
@ControllerAdvice
@Slf4j
public class GenericExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse handleResourceNotFoundException(
            HttpServletRequest req,
            ResourceNotFoundException exception
            ){

        return ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleBadRequestException(
            HttpServletRequest req,
            BadRequestException exception
    ){

        return ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorResponse handleGenericException(
            HttpServletRequest req,
            Exception exception
    ){
        log.error("Terjadi error dengan status code: {}error message: {}", HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());

        return ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleValidationException(
            HttpServletRequest req,
            MethodArgumentNotValidException exception
    ){
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach(
                objectError -> {
                   String fieldName = ((FieldError) objectError).getField();
                   String errorMessage = objectError.getDefaultMessage();
                   errors.put(fieldName, errorMessage);
                });

        return ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(errors.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }
}

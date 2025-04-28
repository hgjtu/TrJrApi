package com.course.travel_journal_web_service.controllers;

import com.course.travel_journal_web_service.CustomExceptions.ResourceNotFoundException;
import com.course.travel_journal_web_service.CustomExceptions.StorageUnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));

        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handlePathVariableError(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach(constraintViolation -> errors.add(constraintViolation.getMessage()));

        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(String.format("Параметр '%s' должен быть типа %s",
                ex.getName(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "неизвестно"));

        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(String.format("Параметр '%s' обязателен (тип: %s)",
                ex.getParameterName(), ex.getParameterType()));

        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        List<String> errors = new ArrayList<>();
        String errorMessage = "Неверный формат тела запроса";
        if (ex.getCause() != null && ex.getCause().getMessage() != null) {
            errorMessage += ": " + ex.getCause().getMessage();
        }
        errors.add(errorMessage);

        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(StorageUnavailableException.class)
    public ResponseEntity<Object> handleStorageUnavailable(StorageUnavailableException ex) {
        String apiError = ex.getMessage();

        List<String> errors = new ArrayList<>();
//        String errorMessage = "";
//        if (ex.getCause() != null && ex.getCause().getMessage() != null) {
//            errorMessage += ": " + ex.getCause().getMessage();
//        }
        errors.add(apiError);

        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<Object> handleInternalAuthenticationService(InternalAuthenticationServiceException ex) {
        String apiError = ex.getMessage();

        List<String> errors = new ArrayList<>();
//        String errorMessage = "";
//        if (ex.getCause() != null && ex.getCause().getMessage() != null) {
//            errorMessage += ": " + ex.getCause().getMessage();
//        }
        errors.add(apiError);

        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        String apiError = ex.getMessage();

        List<String> errors = new ArrayList<>();
//        String errorMessage = "";
//        if (ex.getCause() != null && ex.getCause().getMessage() != null) {
//            errorMessage += ": " + ex.getCause().getMessage();
//        }
        errors.add(apiError);

        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(NoResourceFoundException ex) {
        String apiError = ex.getMessage();

        List<String> errors = new ArrayList<>();
//        String errorMessage = "";
//        if (ex.getCause() != null && ex.getCause().getMessage() != null) {
//            errorMessage += ": " + ex.getCause().getMessage();
//        }
        errors.add(apiError);

        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        String apiError = ex.getMessage();

        List<String> errors = new ArrayList<>();
//        String errorMessage = "";
//        if (ex.getCause() != null && ex.getCause().getMessage() != null) {
//            errorMessage += ": " + ex.getCause().getMessage();
//        }
        errors.add(apiError);

        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        List<String> errors = new ArrayList<>();
        errors.add("Доступ запрещен");

        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        String apiError = ex.getMessage();

        List<String> errors = new ArrayList<>();
//        String errorMessage = "";
//        if (ex.getCause() != null && ex.getCause().getMessage() != null) {
//            errorMessage += ": " + ex.getCause().getMessage();
//        }
        errors.add(apiError);

        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
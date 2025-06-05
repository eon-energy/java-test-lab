package com.technokratos.handler;

import com.technokratos.dto.response.error.ErrorResponse;
import com.technokratos.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> Optional.of(fieldError.getDefaultMessage()).orElse("")
                ));

        ErrorResponse response = new ErrorResponse(
                status.value(),
                ex.getMessage(),
                request.getDescription(false),
                status.getReasonPhrase(),
                errors,
                Instant.now());


        return new ResponseEntity<>(response, status);

    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ServiceException ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                ex.getStatus().value(),
                ex.getMessage(),
                request.getDescription(false),
                ex.getStatus().getReasonPhrase(),
                Collections.emptyMap(),
                Instant.now());
        return new ResponseEntity<>(response, ex.getStatus());
    }


}

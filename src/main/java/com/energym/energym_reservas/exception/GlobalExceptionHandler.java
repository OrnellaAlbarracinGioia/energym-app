package com.energym.energym_reservas.exception;

import com.energym.energym_reservas.dto.response.ApiErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.energym.energym_reservas.controller")
public class GlobalExceptionHandler {

    //404 (Not Found)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    //409 (Conflict)
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiErrorDTO> handleBusinessRule(BusinessRuleException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    //404 (Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return buildResponse(HttpStatus.BAD_REQUEST, "Error de validación", request, errors);
    }

    //500 (Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleGeneral(Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno inesperado: " + ex.getMessage(), request, null);
    }

    private ResponseEntity<ApiErrorDTO> buildResponse(HttpStatus status, String message, HttpServletRequest req, Map<String, String> errors) {
        return ResponseEntity.status(status).body(ApiErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(req.getRequestURI())
                .validationErrors(errors)
                .build());
    }
}

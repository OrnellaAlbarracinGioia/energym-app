package com.energym.energym_reservas.exception;

import com.energym.energym_reservas.dto.error.ApiErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 409 Conflict
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiErrorDTO> handleBusinessRule(BusinessRuleException ex, HttpServletRequest request) {
        return construirRespuesta(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    // 404 Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request){
        return construirRespuesta(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    //500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleGeneral(Exception ex, HttpServletRequest request) {
        log.error("Error interno: ", ex);
        return construirRespuesta(HttpStatus.INTERNAL_SERVER_ERROR,
    "Ocurrió un error interno inesperado.", request);
    }

    //404 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        return construirRespuesta(HttpStatus.BAD_REQUEST, "Error de Validación", request);
    }

    private ResponseEntity<ApiErrorDTO> construirRespuesta(HttpStatus status, String message, HttpServletRequest request) {
        ApiErrorDTO apiError = ApiErrorDTO.builder()
                .message(message)
                .status(status.value())
                .error(status.getReasonPhrase())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, status);
    }
}

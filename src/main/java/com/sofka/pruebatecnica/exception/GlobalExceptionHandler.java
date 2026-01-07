package com.sofka.pruebatecnica.exception;
import com.sofka.pruebatecnica.service.dto.ErrorResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleBusinessException(BusinessException ex) {
        log.warn("Business error: {}", ex.getMessage());
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage(),HttpStatus.BAD_REQUEST.value(),LocalDateTime.now());
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error));
    }

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleNotFoundException(NotFoundException ex) {
        log.warn("Not found: {}", ex.getMessage());
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage(),HttpStatus.NOT_FOUND.value(),LocalDateTime.now());
        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleGenericException(Exception ex) {
        log.error("Unexpected error", ex);
        ErrorResponseDto error = new ErrorResponseDto("Internal server error",HttpStatus.INTERNAL_SERVER_ERROR.value(),LocalDateTime.now());
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleValidationException(WebExchangeBindException ex) {
        // Toma el primer mensaje de error de validación
        String errorMessage = ex.getFieldErrors().stream()
                .map(err -> err.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        log.warn("Validation error: {}", errorMessage);
        ErrorResponseDto error = new ErrorResponseDto(
                errorMessage,
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }

}

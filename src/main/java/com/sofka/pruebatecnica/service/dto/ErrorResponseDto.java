package com.sofka.pruebatecnica.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private String message;
    private int status;
    private LocalDateTime timestamp;
}

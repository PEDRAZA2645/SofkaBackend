package com.sofka.pruebatecnica.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDto {

    private Long id;
    private BigDecimal amount;
    private BigDecimal commission;
    private LocalDateTime creationDate;
}


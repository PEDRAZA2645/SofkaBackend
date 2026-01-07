package com.sofka.pruebatecnica.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDto {

    @NotNull(message = "The amount cannot be null", groups = {Create.class, Update.class})
    @Positive(message = "Amount must be positive", groups = {Create.class, Update.class})
    private BigDecimal amount;

    public interface Create {}
    public interface Update {}
}


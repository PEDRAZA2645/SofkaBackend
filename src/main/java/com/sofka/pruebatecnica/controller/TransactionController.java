package com.sofka.pruebatecnica.controller;

import com.sofka.pruebatecnica.service.ITransactionService;
import com.sofka.pruebatecnica.service.dto.PageResponseDto;
import com.sofka.pruebatecnica.service.dto.TransactionRequestDto;
import com.sofka.pruebatecnica.service.dto.TransactionResponseDto;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Validated
@Log4j2
public class TransactionController {

    private final ITransactionService transactionService;

    @GetMapping("/getAll")
    public Mono<PageResponseDto<TransactionResponseDto>> getAll(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        log.info("HTTP GET /transactions/getAll?page={}&size={}", page, size);
        return transactionService.getAllTransactions(page, size);
    }

    @PostMapping("/addNew")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TransactionResponseDto> create(
            @RequestBody @Validated(TransactionRequestDto.Create.class) TransactionRequestDto request) {
        log.info("HTTP POST /transactions/addNew");
        return transactionService.createTransaction(request);
    }

    @PutMapping("/updateData/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TransactionResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Validated(TransactionRequestDto.Update.class) TransactionRequestDto request) {
        log.info("HTTP POST /transactions/addUpdate");
        return transactionService.updateTransaction(id, request);
    }
}


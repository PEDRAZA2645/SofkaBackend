package com.sofka.pruebatecnica.service;

import com.sofka.pruebatecnica.service.dto.PageResponseDto;
import com.sofka.pruebatecnica.service.dto.TransactionRequestDto;
import com.sofka.pruebatecnica.service.dto.TransactionResponseDto;
import reactor.core.publisher.Mono;

public interface ITransactionService {
    Mono<PageResponseDto<TransactionResponseDto>> getAllTransactions(int page,int size);
    Mono<TransactionResponseDto> createTransaction(TransactionRequestDto dto) ;
    Mono<TransactionResponseDto> updateTransaction(Long id,TransactionRequestDto transactionRequestDto);

}

package com.sofka.pruebatecnica.service.impl;

import com.sofka.pruebatecnica.exception.BusinessException;
import com.sofka.pruebatecnica.exception.NotFoundException;
import com.sofka.pruebatecnica.persistence.entity.TransactionEntity;
import com.sofka.pruebatecnica.persistence.repository.ITransactionRepository;
import com.sofka.pruebatecnica.service.ITransactionService;
import com.sofka.pruebatecnica.service.dto.PageResponseDto;
import com.sofka.pruebatecnica.service.dto.TransactionRequestDto;
import com.sofka.pruebatecnica.service.dto.TransactionResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Log4j2
@RequiredArgsConstructor
public class TransactionImpl implements ITransactionService {
    private final ITransactionRepository transactionRepository;

    @Override
    public Mono<TransactionResponseDto> createTransaction(TransactionRequestDto transactionRequestDto) {

        return Mono.just(transactionRequestDto)
                .doOnNext(dto -> log.info("Starting transaction creation with amount {}", dto.getAmount()))
                .filter(dto -> dto.getAmount() != null && dto.getAmount().compareTo(BigDecimal.ZERO) > 0)
                .switchIfEmpty(Mono.error(new BusinessException("Amount must be greater than zero")))
                .map(dto -> {
                    LocalDateTime now = LocalDateTime.now();
                    BigDecimal commission = calculateCommission(dto.getAmount());
                    return new TransactionEntity(null,dto.getAmount(),commission,now,null);
                })
                .flatMap(transactionRepository::save)
                .map(this::parseToResponseDto);
    }

    @Override
    public Mono<TransactionResponseDto> updateTransaction(Long id,TransactionRequestDto transactionRequestDto) {
        return transactionRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Transaction not found")))
                .flatMap(entity -> {
                    entity.setAmount(transactionRequestDto.getAmount());
                    entity.setCommission(calculateCommission(transactionRequestDto.getAmount()));
                    entity.setUpdateDate(LocalDateTime.now());
                    return transactionRepository.save(entity);
                })
                .map(this::parseToResponseDto);
    }


    @Override
    public Mono<PageResponseDto<TransactionResponseDto>> getAllTransactions(int page, int size) {
        final int safePage = Math.max(page, 1);
        final int safeSize = Math.max(size, 1);
        final int offset = (safePage - 1) * safeSize;
        log.info("Fetching transactions page {} with size {}", safePage, safeSize);
        Mono<Long> total = transactionRepository.countAll()
                .doOnNext(count -> log.debug("Total transactions found {}", count));
        Flux<TransactionResponseDto> data = transactionRepository
                .findAllPaged(safeSize, offset)
                .map(this::parseToResponseDto);
        return total.zipWith(data.collectList())
                .flatMap(tuple -> {
                    if (tuple.getT1() == 0) {
                        return Mono.error(
                                new NotFoundException("No transactions found")
                        );
                    }
                    log.info("Paginated transaction search completed");
                    return Mono.just(new PageResponseDto<>(tuple.getT1(),safePage,safeSize,tuple.getT2()));
                });
    }


    private BigDecimal calculateCommission(BigDecimal amount) {
        return amount.compareTo(BigDecimal.valueOf(10_000)) >= 0
                ? amount.multiply(BigDecimal.valueOf(0.05))
                : amount.multiply(BigDecimal.valueOf(0.02));
    }

    private TransactionEntity parseToEntity(TransactionRequestDto dto) {
        BigDecimal commission = calculateCommission(dto.getAmount());
        return new TransactionEntity(null,dto.getAmount(),commission,LocalDateTime.now(),LocalDateTime.now());
    }

    private TransactionResponseDto parseToResponseDto(TransactionEntity entity) {
        return new TransactionResponseDto(entity.getId(),entity.getAmount(),entity.getCommission(),entity.getCreationDate());
    }
}

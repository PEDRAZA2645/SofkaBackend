package com.sofka.pruebatecnica.controller;

import com.sofka.pruebatecnica.exception.GlobalExceptionHandler;
import com.sofka.pruebatecnica.service.ITransactionService;
import com.sofka.pruebatecnica.service.dto.PageResponseDto;
import com.sofka.pruebatecnica.service.dto.TransactionRequestDto;
import com.sofka.pruebatecnica.service.dto.TransactionResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private ITransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(transactionController)
                .controllerAdvice(new GlobalExceptionHandler())
                .build();
    }


    @Test
    void createTransaction() {
        TransactionResponseDto mockResponse = new TransactionResponseDto(
                BigDecimal.valueOf(15000),
                BigDecimal.valueOf(750),
                LocalDateTime.now()
        );

        Mockito.when(transactionService.createTransaction(Mockito.any()))
                .thenReturn(Mono.just(mockResponse));

        TransactionRequestDto request = new TransactionRequestDto();
        request.setAmount(BigDecimal.valueOf(15000));

        webTestClient.post()
                .uri("/transactions/addNew")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.amount").value(a -> assertEquals(15000, ((Number) a).intValue()))
                .jsonPath("$.commission").value(c -> assertEquals(750.0, ((Number) c).doubleValue()));
    }

    @Test
    void amountIsNegative() {
        webTestClient.post()
                .uri("/transactions/addNew")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"amount\": -100}")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getAllTransactions() {
        TransactionResponseDto t1 = new TransactionResponseDto(BigDecimal.valueOf(1000), BigDecimal.valueOf(50), LocalDateTime.now());
        TransactionResponseDto t2 = new TransactionResponseDto(BigDecimal.valueOf(2000), BigDecimal.valueOf(100), LocalDateTime.now());
        PageResponseDto<TransactionResponseDto> pageResponse = new PageResponseDto<>();
        pageResponse.setContent(List.of(t1, t2));
        pageResponse.setPage(1);
        pageResponse.setSize(2);
        pageResponse.setTotalElements(2);
        Mockito.when(transactionService.getAllTransactions(1, 10))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/transactions/getAll")
                        .queryParam("page", 1)
                        .queryParam("size", 10)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content.length()").isEqualTo(2)
                .jsonPath("$.content[0].amount").isEqualTo(1000)
                .jsonPath("$.content[1].amount").isEqualTo(2000)
                .jsonPath("$.page").isEqualTo(1)
                .jsonPath("$.size").isEqualTo(2)
                .jsonPath("$.totalElements").isEqualTo(2);
    }
}

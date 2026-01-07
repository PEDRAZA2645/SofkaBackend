package com.sofka.pruebatecnica.persistence.repository;

import com.sofka.pruebatecnica.persistence.entity.TransactionEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITransactionRepository extends ReactiveCrudRepository<TransactionEntity, Long> {

    @Query("""
        SELECT * FROM transactions
        ORDER BY creation_date DESC
        OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY
    """)
    Flux<TransactionEntity> findAllPaged(int limit, long offset);
    @Query("SELECT COUNT(*) FROM transactions")
    Mono<Long> countAll();
}


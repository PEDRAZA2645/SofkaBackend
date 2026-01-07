package com.sofka.pruebatecnica.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("transactions")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class TransactionEntity {

    @Id
    private Long id;

    @Column("amount")
    private BigDecimal amount;

    @Column("commission")
    private BigDecimal commission;

    @Column("creation_date")
    private LocalDateTime creationDate;

    @Column("update_date")
    private LocalDateTime updateDate;

}

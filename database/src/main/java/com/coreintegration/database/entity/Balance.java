package com.coreintegration.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "balance")
@EqualsAndHashCode
public class Balance {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "account_id", insertable = false, updatable = false)
    private UUID accountId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "total_balance")
    private BigDecimal totalBalance;

    @Column(name = "available_balance")
    private BigDecimal availableBalance;

    @Column(name = "currency")
    private String currency;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

}

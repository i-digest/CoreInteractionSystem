package com.coreintegration.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table(name = "limit")
@EqualsAndHashCode
public class Limit {

    @Column(name = "id")
    private UUID id;

    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "daily_limit")
    private BigDecimal dailyLimit;

    @Column(name = "weekly_limit")
    private BigDecimal weeklyLimit;

    @Column(name = "monthly_limit")
    private BigDecimal monthlyLimit;

    @Column(name = "yearly_limit")
    private BigDecimal yearlyLimit;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

}

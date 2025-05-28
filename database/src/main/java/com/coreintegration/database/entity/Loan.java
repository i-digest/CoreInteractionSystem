package com.coreintegration.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "loan")
@EqualsAndHashCode
public class Loan {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "account_id", insertable = false, updatable = false)
    private UUID accountId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "loan_number")
    private String loanNumber;

    @Column(name = "principal_amount")
    private BigDecimal principalAmount;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

}

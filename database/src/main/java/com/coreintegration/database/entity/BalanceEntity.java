package com.coreintegration.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Table(name = "balance")
@EqualsAndHashCode
public class BalanceEntity {

    @Column(name = "id")
    private String id;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

}

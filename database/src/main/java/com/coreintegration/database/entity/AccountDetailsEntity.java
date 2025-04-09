package com.coreintegration.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(name = "account_details")
public class AccountDetailsEntity {

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

}

package com.coreintegration.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table(name = "legal_entity")
@EqualsAndHashCode
public class LegalEntity {

    @Column(name = "id")
    private UUID id;

    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "name")
    private String name;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

}

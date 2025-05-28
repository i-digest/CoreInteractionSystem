package com.coreintegration.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "legal_entity")
@EqualsAndHashCode
public class LegalEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "account_id", insertable = false, updatable = false)
    private UUID accountId;

    @Column(name = "name")
    private String name;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

}

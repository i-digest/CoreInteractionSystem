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
@Table(name = "debit_card")
@EqualsAndHashCode
public class DebitCard {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "account_id", insertable = false, updatable = false)
    private UUID accountId;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "card_expiry_date")
    private LocalDateTime cardExpiryDate;

    @Column(name = "card_holder_name")
    private String cardHolderName;

    @Column(name = "card_issue_date")
    private LocalDateTime cardIssueDate;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

}

package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;
    private Long recipientId;
    private float amount;

    // 🔹 NEW FIELD (required for Task 4)
    private float incentive;

    private Instant timestamp;

    protected TransactionRecord() {
        // JPA only
    }

    // 🔹 Used in Task 3 (no incentive)
    public TransactionRecord(Long senderId, Long recipientId, float amount) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
        this.incentive = 0f;
        this.timestamp = Instant.now();
    }

    // 🔹 Used in Task 4 (WITH incentive)
    public TransactionRecord(long senderId, long recipientId, float amount, float incentive) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
        this.incentive = incentive;
        this.timestamp = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public float getAmount() {
        return amount;
    }

    public float getIncentive() {
        return incentive;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}

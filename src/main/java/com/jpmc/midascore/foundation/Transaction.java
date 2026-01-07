package com.jpmc.midascore.foundation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

    private long senderId;
    private long recipientId;
    private float amount;

    // Required for Kafka / Jackson
    public Transaction() {}

    public Transaction(long senderId, long recipientId, float amount) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
    }

    public long getSenderId() {
        return senderId;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public float getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "senderId=" + senderId +
                ", recipientId=" + recipientId +
                ", amount=" + amount +
                '}';
    }
}

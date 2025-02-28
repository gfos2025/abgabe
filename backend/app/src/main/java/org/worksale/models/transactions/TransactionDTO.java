package org.worksale.models.transactions;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TransactionDTO {
    private UUID id;
    private UUID customerId;
    private UUID userId;
    private double amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime timestamp;
    private TransactionStatus status;
    private String description;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.customerId = transaction.getCustomer().getId();
        this.userId = transaction.getUser().getId();
        this.amount = transaction.getAmount();
        this.timestamp = transaction.getTimestamp();
        this.status = transaction.getStatus();
        this.description = transaction.getDescription();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
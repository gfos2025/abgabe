package org.worksale.requests;

import java.time.LocalDateTime;
import java.util.UUID;

import org.worksale.models.transactions.TransactionStatus;

public class CreateTransaction {
    public UUID customerId;
    public UUID userId;
    public TransactionStatus status;
    public double amount;
    public String description;
    public LocalDateTime timestamp;

    public CreateTransaction() {
    }
}

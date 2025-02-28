package org.worksale.requests;

import java.time.LocalDateTime;
import java.util.UUID;

import org.worksale.models.transactions.TransactionStatus;

public class GetTransactionReport {
    public LocalDateTime startDate;
    public LocalDateTime endDate;
    public TransactionStatus status;
    public String userId;
    public String customerId;

    public GetTransactionReport() {
    }
}

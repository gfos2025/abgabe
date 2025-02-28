package org.worksale.requests;

import org.worksale.models.transactions.TransactionStatus;

public class UpdateTransaction {
    public TransactionStatus status;
    public Double amount;
    public String description;
}

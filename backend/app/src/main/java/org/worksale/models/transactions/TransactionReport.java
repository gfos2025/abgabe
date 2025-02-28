package org.worksale.models.transactions;

import java.util.Map;

public class TransactionReport {
    private int totalTransactions;
    private double totalRevenue;
    private double averageTransactionAmount;
    private Map<TransactionStatus, Integer> transactionsByStatus;

    public TransactionReport() {
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double TotalRevenue) {
        this.totalRevenue = TotalRevenue;
    }

    public double getAverageTransactionAmount() {
        return averageTransactionAmount;
    }

    public void setAverageTransactionAmount(double averageTransactionAmount) {
        this.averageTransactionAmount = averageTransactionAmount;
    }

    public Map<TransactionStatus, Integer> getTransactionsByStatus() {
        return transactionsByStatus;
    }

    public void setTransactionsByStatus(Map<TransactionStatus, Integer> transactionsByStatus) {
        this.transactionsByStatus = transactionsByStatus;
    }
}

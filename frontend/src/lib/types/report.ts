export interface ReportData {
    totalTransactions: number;
    totalRevenue: number;
    averageTransactionAmount: number;
    transactionsByStatus: {
        Completed: number;
        Failed: number;
        Pending: number;
    };
}

export interface MonthlyReport {
    month: string;
    reportData: ReportData;
}
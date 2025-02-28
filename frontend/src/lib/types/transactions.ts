export enum TransactionStatus {
  Completed = 0,
  Pending = 1,
  Failed = 2
}

export const transactionStatusMap: { [key in TransactionStatus]: string } = {
  [TransactionStatus.Completed]: 'status.completed',
  [TransactionStatus.Pending]: 'status.pending',
  [TransactionStatus.Failed]: 'status.failed'
};

export type Transaction = {
  id: string;
  customerId: string;
  userId: string;
  amount: number;
  description?: string;
  timestamp: string;
  status: TransactionStatus;
};
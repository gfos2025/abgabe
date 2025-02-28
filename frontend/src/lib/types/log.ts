export type Log = {
    id: string;
    message: string;
    type: LogType;
    timestamp: string;
    userId: string | null;
};

export enum LogType {
    REGISTERED_USER = 0,
    CREATED_CUSTOMER = 1,
    UPDATED_CUSTOMER = 2,
    DELETED_CUSTOMER = 3,
    CREATED_TRANSACTION = 4,
    UPDATED_TRANSACTION = 5,
    CREATED_APPOINTMENT = 6,
    UPDATED_APPOINTMENT = 7,
    DELETED_APPOINTMENT = 8,
}
package org.worksale.models;

/*
 * Used to specify the type of log entry.
 */
public enum LogType {
    REGISTERED_USER,
    CREATED_CUSTOMER,
    UPDATED_CUSTOMER,
    DELETED_CUSTOMER,
    CREATED_TRANSACTION,
    UPDATED_TRANSACTION,
    CREATED_APPOINTMENT,
    UPDATED_APPOINTMENT,
    DELETED_APPOINTMENT,
}

package org.worksale.controllers;

import java.time.LocalDateTime;
import java.util.*;
import org.worksale.models.LogType;
import org.worksale.models.transactions.*;
import org.worksale.models.users.User;
import org.worksale.requests.*;
import org.worksale.services.*;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;

/**
 * Controller for handling transaction-related requests.
 */
public class TransactionController {

    /**
     * Creates a new transaction with validation checks.
     *
     * @param ctx The Javalin context containing the request data and response.
     * @throws ServiceException If the transaction could not be created.
     */
    public void createTransaction(Context ctx) throws ServiceException {
        User ctxUser = ctx.sessionAttribute("user"); // Get the user from session
        LocalDateTime timestamp = LocalDateTime.now(); // Current timestamp

        CreateTransaction transaction = ctx.bodyValidator(CreateTransaction.class)
                .check(tr -> tr.userId != null, "User is required") // Validate user ID
                .check(tr -> tr.customerId != null, "Customer is required") // Validate customer ID
                .check(tr -> tr.timestamp != null, "Timestamp is required") // Validate timestamp
                .check(tr -> tr.timestamp.isBefore(timestamp), "Timestamp must be in the past") // Check past timestamp
                .check(tr -> tr.timestamp.isAfter(timestamp.minusDays(90)), "Timestamp must be within the last 90 days") // Check
                                                                                                                         // 90-day
                                                                                                                         // limit
                .get();

        TransactionService.createTransaction(transaction); // Create the transaction
        LogService.createEntry(
                String.format("User %s %s created a new transaction with amount %s", ctxUser.getName(),
                        ctxUser.getSurname(), transaction.amount),
                LogType.CREATED_TRANSACTION, ctxUser.getId()); // Log the action
        ctx.status(201).result("Transaction successfully created"); // Respond with success
    }

    /**
     * Retrieves a list of transactions matching the given criteria.
     *
     * @param ctx The Javalin context containing the request data and response.
     * @throws ServiceException If an error occurs while retrieving transactions.
     */
    public void getTransactions(Context ctx) throws ServiceException {
        Optional<UUID> userId = Optional.ofNullable(ctx.queryParam("user_id")).map(UUID::fromString); // Parse user ID
        Optional<UUID> customerId = Optional.ofNullable(ctx.queryParam("customer_id")).map(UUID::fromString); // Parse
                                                                                                              // customer
                                                                                                              // ID
        Optional<TransactionStatus> status = Optional.ofNullable(ctx.queryParam("status"))
                .map(TransactionStatus::valueOf); // Parse status

        List<TransactionDTO> transactions = TransactionService.getTransactions(userId, customerId, status); // Fetch
                                                                                                            // transactions
        ctx.status(200).json(transactions); // Return as JSON
    }

    /**
     * Retrieves a transaction by its ID.
     *
     * @param ctx The Javalin context containing the request data and response.
     * @throws ServiceException If an error occurs while retrieving the transaction.
     */
    public void getTransaction(Context ctx) throws ServiceException {
        String id = ctx.pathParam("id"); // Get transaction ID from path
        try {
            UUID uuid = UUID.fromString(id); // Parse UUID
            TransactionDTO transaction = TransactionService.getTransactionById(uuid); // Fetch transaction
            if (transaction == null) {
                throw new ServiceException("Transaction not found"); // Handle not found
            }
            ctx.status(200).json(transaction); // Return as JSON
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Invalid transaction ID"); // Handle invalid ID
        }
    }

    /**
     * Validates if a transaction status transition is allowed.
     *
     * @param currentStatus The current status of the transaction.
     * @param newStatus     The desired new status of the transaction.
     * @return True if the transition is valid, false otherwise.
     */
    private boolean isValidStatusTransition(TransactionStatus currentStatus, TransactionStatus newStatus) {
        return switch (currentStatus) {
            case Pending -> Set.of(TransactionStatus.Completed, TransactionStatus.Failed).contains(newStatus); // Allow
                                                                                                               // Pending
                                                                                                               // ->
                                                                                                               // Completed/Failed
            case Completed, Failed -> false; // Disallow changes from Completed or Failed
        };
    }

    /**
     * Updates the status of a transaction.
     *
     * @param ctx The Javalin context containing the request data and response.
     * @throws ServiceException   If an unexpected error occurs while updating the
     *                            transaction.
     * @throws BadRequestResponse If the status transition is invalid.
     */
    public void updateTransaction(Context ctx) throws ServiceException {
        User ctxUser = ctx.sessionAttribute("user"); // Get user from session
        String id = ctx.pathParam("id"); // Get transaction ID from path
        UpdateTransaction data = ctx.bodyAsClass(UpdateTransaction.class); // Parse request body

        TransactionDTO txDTO = TransactionService.getTransactionById(UUID.fromString(id)); // Fetch transaction
        if (txDTO == null) {
            throw new ServiceException("Transaction not found"); // Handle not found
        }

        if (data.status != null && !isValidStatusTransition(txDTO.getStatus(), data.status)) { // Validate status
                                                                                               // transition
            throw new BadRequestResponse("Invalid status transition");
        }

        TransactionService.updateTransaction(UUID.fromString(id), data); // Update transaction
        LogService.createEntry(
                String.format("User %s %s updated transaction with ID %s", ctxUser.getName(), ctxUser.getSurname(), id),
                LogType.UPDATED_TRANSACTION, ctxUser.getId()); // Log the action
        ctx.status(200).result("Transaction successfully updated"); // Respond with success
    }

    /**
     * Generates a transaction report based on the provided criteria.
     *
     * @param ctx The Javalin context containing the request data and response.
     * @throws ServiceException If an error occurs while generating the report.
     */
    public void generateReport(Context ctx) throws ServiceException {
        GetTransactionReport reportCriteria = ctx.bodyAsClass(GetTransactionReport.class); // Parse request body
        ctx.status(200).json(TransactionService.generateReport(reportCriteria)); // Generate and return report
    }
}
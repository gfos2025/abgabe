package org.worksale.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.worksale.Database;
import org.worksale.models.customers.Customer;
import org.worksale.models.transactions.*;
import org.worksale.models.users.User;
import org.worksale.requests.*;

/**
 * Provides services for managing transaction-related operations such as
 * creating,
 * retrieving, updating, and generating reports for transactions.
 */
public class TransactionService {

    // Logger instance for logging errors and other important information
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    /**
     * Creates a new transaction in the database.
     *
     * @param transaction_data The data for the transaction to be created.
     * @throws ServiceException If an error occurs during the creation process.
     */
    public static void createTransaction(CreateTransaction transaction_data) throws ServiceException {
        EntityManager em = Database.getEntityManager(); // Get the entity manager
        EntityTransaction tx = em.getTransaction(); // Begin a transaction

        try {
            User user = UserService.getUserById(transaction_data.userId); // Retrieve the user
            if (user == null) {
                throw new ServiceException("User not found"); // Throw exception if user is not found
            }

            Customer customer = CustomerService.getCustomerById(transaction_data.customerId); // Retrieve the customer
            if (customer == null) {
                throw new ServiceException("Customer not found"); // Throw exception if customer is not found
            }

            Transaction transaction = new Transaction(); // Create a new transaction object
            transaction.setUser(user);
            transaction.setCustomer(customer);
            transaction.setAmount(transaction_data.amount);
            transaction.setDescription(transaction_data.description);
            transaction.setTimestamp(transaction_data.timestamp);
            Optional.ofNullable(transaction_data.status).ifPresent(transaction::setStatus);

            tx.begin(); // Start the transaction
            em.persist(transaction); // Persist the transaction
            tx.commit(); // Commit the transaction
        } catch (ServiceException e) {
            tx.rollback(); // Rollback in case of a service exception
            throw e;
        } catch (Exception e) {
            tx.rollback(); // Rollback in case of any other exception
            logger.error("Error creating transaction: " + e.getMessage());
            throw new ServiceException("An unexpected error occurred while creating the transaction");
        } finally {
            em.close(); // Close the entity manager
        }
    }

    /**
     * Retrieves transactions based on optional filters.
     *
     * @param user_id     Optional user ID filter.
     * @param customer_id Optional customer ID filter.
     * @param status      Optional transaction status filter.
     * @return A list of transaction DTOs matching the criteria.
     * @throws ServiceException If an error occurs during retrieval.
     */
    public static List<TransactionDTO> getTransactions(Optional<UUID> user_id, Optional<UUID> customer_id,
            Optional<TransactionStatus> status) throws ServiceException {
        EntityManager em = Database.getEntityManager(); // Get the entity manager

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder(); // Create a CriteriaBuilder instance
            CriteriaQuery<Transaction> query = cb.createQuery(Transaction.class); // Create a CriteriaQuery for
                                                                                  // Transaction entities
            Root<Transaction> root = query.from(Transaction.class); // Define the root entity for the query

            List<Predicate> predicates = new ArrayList<>(); // List to hold query conditions

            // Add predicates based on the provided filters
            user_id.ifPresent(id -> predicates.add(cb.equal(root.get("user").get("id"), id)));
            customer_id.ifPresent(id -> predicates.add(cb.equal(root.get("customer").get("id"), id)));
            status.ifPresent(s -> predicates.add(cb.equal(root.get("status"), s)));

            // Build the query with predicates
            if (predicates.isEmpty()) {
                query.select(root); // Select all transactions if no filters are provided
            } else {
                query.select(root).where(cb.and(predicates.toArray(new Predicate[0]))); // Apply predicates
            }

            return em.createQuery(query).getResultList().stream() // Execute the query
                    .map(TransactionDTO::new) // Convert each transaction to a DTO
                    .toList(); // Return as a list
        } catch (Exception e) {
            logger.error("Error getting transactions: " + e.getMessage());
            throw new ServiceException("An unexpected error occurred while getting the transactions");
        } finally {
            em.close(); // Close the entity manager
        }
    }

    /**
     * Retrieves a transaction by its unique ID.
     *
     * @param id The UUID of the transaction to retrieve.
     * @return The transaction DTO if found, otherwise null.
     * @throws ServiceException If an error occurs during retrieval.
     */
    public static TransactionDTO getTransactionById(UUID id) throws ServiceException {
        EntityManager em = Database.getEntityManager(); // Get the entity manager

        try {
            Transaction transaction = em.find(Transaction.class, id); // Find the transaction by ID
            return transaction != null ? new TransactionDTO(transaction) : null; // Convert to DTO if found
        } catch (Exception e) {
            logger.error("Error getting transaction by id: " + e.getMessage()); // Log the error
            throw new ServiceException("An unexpected error occurred while getting the transaction");
        } finally {
            em.close(); // Close the entity manager
        }
    }

    /**
     * Updates an existing transaction's details.
     *
     * @param id   The UUID of the transaction to update.
     * @param data The updated transaction data.
     * @throws ServiceException If the transaction is not found or an error occurs
     *                          during the update.
     */
    public static void updateTransaction(UUID id, UpdateTransaction data) throws ServiceException {
        EntityManager em = Database.getEntityManager(); // Get the entity manager
        EntityTransaction tx = em.getTransaction(); // Begin a transaction

        try {
            tx.begin(); // Start the transaction
            Transaction transaction = em.find(Transaction.class, id); // Find the transaction by ID

            if (transaction == null) {
                throw new ServiceException("Transaction not found"); // Throw exception if transaction is not found
            }

            // Update transaction fields if provided
            if (data.amount != null) {
                transaction.setAmount(data.amount);
            }
            if (data.description != null) {
                transaction.setDescription(data.description);
            }
            if (data.status != null) {
                transaction.setStatus(data.status);
            }

            tx.commit(); // Commit the transaction
        } catch (Exception e) {
            tx.rollback(); // Rollback in case of an exception
            logger.error("Error updating transaction: " + e.getMessage());
            throw new ServiceException("An unexpected error occurred while updating the transaction");
        } finally {
            em.close(); // Close the entity manager
        }
    }

    /**
     * Generates a transaction report based on the provided criteria.
     *
     * @param reportCriteria The criteria for generating the report.
     * @return A transaction report containing summary statistics.
     * @throws ServiceException If an error occurs during report generation.
     */
    public static TransactionReport generateReport(GetTransactionReport reportCriteria) throws ServiceException {
        EntityManager em = null; // Initialize the entity manager

        try {
            em = Database.getEntityManager(); // Get the entity manager
            CriteriaBuilder cb = em.getCriteriaBuilder(); // Create a CriteriaBuilder instance
            CriteriaQuery<Transaction> query = cb.createQuery(Transaction.class); // Create a CriteriaQuery for
                                                                                  // Transaction entities
            Root<Transaction> root = query.from(Transaction.class); // Define the root entity for the query

            List<Predicate> predicates = new ArrayList<>(); // List to hold query conditions

            // Add predicates based on the provided criteria
            Optional.ofNullable(reportCriteria.userId)
                    .ifPresent(id -> predicates.add(cb.equal(root.get("user").get("id"), UUID.fromString(id))));
            Optional.ofNullable(reportCriteria.customerId)
                    .ifPresent(id -> predicates.add(cb.equal(root.get("customer").get("id"), UUID.fromString(id))));
            Optional.ofNullable(reportCriteria.status)
                    .ifPresent(s -> predicates.add(cb.equal(root.get("status"), s)));

            // Add date range predicates if provided
            if (reportCriteria.startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), reportCriteria.startDate));
            }
            if (reportCriteria.endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), reportCriteria.endDate));
            }

            // Exclude failed transactions
            predicates.add(cb.notEqual(root.get("status"), TransactionStatus.Failed));

            // Build the query with predicates
            if (!predicates.isEmpty()) {
                query.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            // Execute the query
            List<Transaction> transactions = em.createQuery(query).getResultList();

            // Build the transaction report
            TransactionReport report = new TransactionReport();
            report.setTotalTransactions(transactions.size());
            report.setTotalRevenue(
                    transactions.stream()
                            .mapToDouble(Transaction::getAmount)
                            .sum());
            report.setAverageTransactionAmount(
                    transactions.stream()
                            .mapToDouble(Transaction::getAmount)
                            .average()
                            .orElse(0.0));
            report.setTransactionsByStatus(
                    transactions.stream()
                            .collect(Collectors.groupingBy(
                                    Transaction::getStatus,
                                    Collectors.summingInt(e -> 1))));

            return report;
        } catch (PersistenceException e) {
            logger.error("Database error generating report", e);
            throw new ServiceException("Failed to generate transaction report");
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
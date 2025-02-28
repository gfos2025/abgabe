package org.worksale.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.worksale.Database;
import org.worksale.models.Log;
import org.worksale.models.LogType;
import org.worksale.requests.GetLogs;

/**
 * Provides services for managing log-related operations such as creating log
 * entries
 * and retrieving logs based on specified criteria.
 */
public class LogService {

    // Logger instance for logging errors and other important information
    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    /**
     * Creates a new log entry in the database.
     *
     * @param message The log message.
     * @param type    The type of log.
     * @param user_id The UUID of the user associated with the log entry.
     * @throws ServiceException If an error occurs during the creation process.
     */
    public static void createEntry(String message, LogType type, UUID user_id) throws ServiceException {
        EntityManager em = Database.getEntityManager(); // Get the entity manager
        EntityTransaction tx = em.getTransaction(); // Begin a transaction
        Log log = new Log(message, type, LocalDateTime.now(), user_id); // Create a new log entry

        try {
            tx.begin(); // Start the transaction
            em.persist(log); // Persist the log entry
            tx.commit(); // Commit the transaction
        } catch (Exception e) {
            tx.rollback(); // Rollback in case of an exception
            logger.error("Error creating log entry: " + e.getMessage()); // Log the error
            throw new ServiceException("An unexpected error occurred while creating log entry");
        } finally {
            em.close(); // Close the entity manager
        }
    }

    /**
     * Retrieves logs from the database based on the provided criteria.
     *
     * @param criteria The log retrieval criteria
     * @return A list of logs matching the criteria.
     * @throws ServiceException If an error occurs during the retrieval process.
     */
    public static List<Log> getLogs(GetLogs criteria) throws ServiceException {
        EntityManager em = Database.getEntityManager(); // Get the entity manager

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder(); // Create a CriteriaBuilder instance
            CriteriaQuery<Log> query = cb.createQuery(Log.class); // Create a CriteriaQuery for Log entities
            Root<Log> logRoot = query.from(Log.class); // Define the root entity for the query

            List<Predicate> predicates = new ArrayList<>(); // List to hold query conditions

            // Add predicates based on the provided criteria
            if (criteria.startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(logRoot.get("timestamp"), criteria.startDate));
            }
            if (criteria.endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(logRoot.get("timestamp"), criteria.endDate));
            }
            if (criteria.userId != null) {
                predicates.add(cb.equal(logRoot.get("user_id"), criteria.userId));
            }
            if (criteria.logType != null) {
                predicates.add(cb.equal(logRoot.get("type"), criteria.logType));
            }

            // Build the query with predicates
            if (predicates.isEmpty()) {
                query.select(logRoot); // Select all logs if no criteria are provided
            } else {
                query.select(logRoot).where(cb.and(predicates.toArray(new Predicate[0]))); // Apply predicates
            }

            return em.createQuery(query).getResultList(); // Execute the query and return results
        } catch (Exception e) {
            logger.error("Error getting logs: " + e.getMessage()); // Log the error
            throw new ServiceException("An unexpected error occurred while getting logs");
        } finally {
            em.close(); // Close the entity manager
        }
    }
}
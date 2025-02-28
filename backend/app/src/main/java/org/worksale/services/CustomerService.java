package org.worksale.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.worksale.Database;
import org.worksale.models.customers.Customer;
import org.worksale.models.users.User;
import org.worksale.requests.UpdateCustomer;
import jakarta.persistence.*;

/**
 * Provides services for managing customer-related operations such as creating,
 * retrieving, updating, and deleting customers.
 */
public class CustomerService {

    // Logger instance for logging errors and other important information
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    /**
     * Creates a new customer in the database.
     *
     * @param customer The customer object to be created.
     * @throws ServiceException If an error occurs during the creation process.
     */
    public static void createCustomer(Customer customer) throws ServiceException {
        EntityManager em = Database.getEntityManager(); // Get the entity manager
        EntityTransaction tx = em.getTransaction(); // Begin a transaction

        try {
            tx.begin(); // Start the transaction
            em.persist(customer); // Persist the customer object
            tx.commit(); // Commit the transaction
        } catch (Exception e) {
            tx.rollback(); // Rollback in case of an exception
            logger.error("Error creating customer: " + e.getMessage()); // Log the error
            throw new ServiceException("An unexpected error occurred while creating the customer");
        } finally {
            em.close(); // Close the entity manager
        }
    }

    /**
     * Retrieves all customers from the database.
     *
     * @return A list of all customers.
     * @throws ServiceException If an error occurs during retrieval.
     */
    public static List<Customer> getCustomers() throws ServiceException {
        EntityManager em = Database.getEntityManager(); // Get the entity manager

        try {
            return em.createQuery("SELECT c FROM Customer c", Customer.class).getResultList(); // Query all customers
        } catch (Exception e) {
            logger.error("Error getting customers: " + e.getMessage()); // Log the error
            throw new ServiceException("An unexpected error occurred while getting customers");
        } finally {
            em.close(); // Close the entity manager
        }
    }

    /**
     * Retrieves a customer by their unique ID.
     *
     * @param id The UUID of the customer to retrieve.
     * @return The customer object if found, otherwise null.
     * @throws ServiceException If an error occurs during retrieval.
     */
    public static Customer getCustomerById(UUID id) throws ServiceException {
        EntityManager em = Database.getEntityManager(); // Get the entity manager

        try {
            return em.find(Customer.class, id); // Find the customer by ID
        } catch (Exception e) {
            logger.error("Error getting customer by id: " + e.getMessage()); // Log the error
            throw new ServiceException("An unexpected error occurred while getting the customer");
        } finally {
            em.close(); // Close the entity manager
        }
    }

    /**
     * Updates an existing customer's details.
     *
     * @param id   The UUID of the customer to update.
     * @param data The updated customer data.
     * @throws ServiceException If the customer is not found or an error occurs
     *                          during the update.
     */
    public static void updateCustomer(UUID id, UpdateCustomer data) throws ServiceException {
        EntityManager em = Database.getEntityManager(); // Get the entity manager
        EntityTransaction tx = em.getTransaction(); // Begin a transaction

        try {
            tx.begin(); // Start the transaction
            Customer customer = em.find(Customer.class, id); // Find the customer by ID

            if (customer == null) {
                throw new ServiceException("Customer not found"); // Throw exception if customer is not found
            }

            // Update customer fields if provided
            Optional.ofNullable(data.name).ifPresent(customer::setName);
            Optional.ofNullable(data.surname).ifPresent(customer::setSurname);
            Optional.ofNullable(data.email).ifPresent(customer::setEmail);
            Optional.ofNullable(data.company).ifPresent(customer::setCompany);
            Optional.ofNullable(data.phoneNumber).ifPresent(customer::setPhoneNumber);
            Optional.ofNullable(data.address).ifPresent(customer::setAddress);
            Optional.ofNullable(data.contactStatus).ifPresent(customer::setContactStatus);

            // Update assigned manager if provided
            Optional.ofNullable(data.assignedManagerId).ifPresent(managerId -> {
                User assignedManager = UserService.getUserById(UUID.fromString(managerId)); // Get the manager by ID
                customer.setAssignedManager(assignedManager); // Set the assigned manager
            });

            tx.commit(); // Commit the transaction
        } catch (Exception e) {
            tx.rollback(); // Rollback in case of an exception
            logger.error("Error updating customer: " + e.getMessage()); // Log the error
            throw new ServiceException("An unexpected error occurred while updating the customer");
        } finally {
            em.close(); // Close the entity manager
        }
    }

    /**
     * Deletes a customer by their unique ID.
     *
     * @param id The UUID of the customer to delete.
     * @throws ServiceException If the customer is not found or an error occurs
     *                          during deletion.
     */
    public static void deleteCustomer(UUID id) throws ServiceException {
        EntityManager em = Database.getEntityManager(); // Get the entity manager
        EntityTransaction tx = em.getTransaction(); // Begin a transaction

        try {
            tx.begin(); // Start the transaction
            Customer customer = em.find(Customer.class, id); // Find the customer by ID

            if (customer == null) {
                throw new ServiceException("Customer not found"); // Throw exception if customer is not found
            }

            em.remove(customer); // Remove the customer from the database
            tx.commit(); // Commit the transaction
        } catch (Exception e) {
            tx.rollback(); // Rollback in case of an exception
            logger.error("Error deleting customer: " + e.getMessage()); // Log the error
            throw new ServiceException("An unexpected error occurred while deleting the customer");
        } finally {
            em.close(); // Close the entity manager
        }
    }
}
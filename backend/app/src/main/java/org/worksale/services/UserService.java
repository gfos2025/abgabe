package org.worksale.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.worksale.Database;
import org.worksale.models.Role;
import org.worksale.models.users.*;
import org.worksale.requests.Register;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import io.javalin.http.Context;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import java.util.List;
import java.util.UUID;
import org.hibernate.exception.ConstraintViolationException;

/**
 * Provides methods for managing user-related operations in the application.
 */
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final Argon2 argon2 = Argon2Factory.create();

    /**
     * Persists a new user in the database.
     *
     * @param user The user to be created.
     * @throws ServiceException If an error occurs during creation.
     */
    public static void createUser(Register registerData) throws ServiceException {
        EntityManager em = Database.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin(); // Start transaction
            String hash = argon2.hash(1, 65536, 1, registerData.password.toCharArray()); // Hash password
            User user = new User(registerData.name, registerData.surname, registerData.email, hash, registerData.role); // Create
                                                                                                                        // new
                                                                                                                        // user
            em.persist(user); // Persist user
            tx.commit(); // Commit transaction
        } catch (ConstraintViolationException e)

        {
            tx.rollback(); // Rollback on failure
            throw new ServiceException("User with this email already exists");
        } catch (Exception e) {
            tx.rollback(); // Rollback on failure
            logger.error("Error creating user: " + e.getMessage());
            throw new ServiceException("An unexpected error occurred while creating the user");
        }
    }

    /**
     * Retrieves all users from the database.
     *
     * @return List of all users.
     * @throws ServiceException If an error occurs during retrieval.
     */
    public static List<User> getAllUsers() throws ServiceException {
        EntityManager em = Database.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder(); // Create criteria builder
            CriteriaQuery<User> query = cb.createQuery(User.class); // Define query
            Root<User> root = query.from(User.class); // Define root entity
            query.select(root); // Select all users
            return em.createQuery(query).getResultList(); // Execute query and return results
        } catch (Exception e) {
            logger.error("Error getting all users: " + e.getMessage());
            throw new ServiceException("An unexpected error occurred while getting all users");
        }
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id The UUID of the user.
     * @return The user object or null if not found.
     */
    public static User getUserById(UUID id) {
        EntityManager em = Database.getEntityManager();
        try {
            return em.find(User.class, id); // Find user by ID
        } catch (Exception e) {
            logger.error("Error getting user by id: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves all employees from the database.
     *
     * @return List of employee DTOs.
     */
    public static List<UserDTO> getEmployees() throws ServiceException {
        EntityManager em = Database.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder(); // Create criteria builder
            CriteriaQuery<User> query = cb.createQuery(User.class); // Define query
            Root<User> root = query.from(User.class); // Define root entity
            query.select(root).where(cb.equal(root.get("role"), Role.EMPLOYEE)); // Filter by role
            List<User> users = em.createQuery(query).getResultList(); // Execute query
            return users.stream().map(UserDTO::new).toList(); // Map to DTOs
        } catch (Exception e) {
            logger.error("Error getting employees: " + e.getMessage());
            throw new ServiceException("An unexpected error occurred while getting employees");
        }
    }

    /**
     * Deletes a user from the database by their ID.
     *
     * @param id The UUID of the user to delete.
     * @throws ServiceException If an error occurs during deletion.
     */
    public static void deleteUserById(UUID id) throws ServiceException {
        EntityManager em = Database.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin(); // Start transaction
            User user = em.find(User.class, id); // Find user by ID
            em.remove(user); // Remove user
            tx.commit(); // Commit transaction
        } catch (Exception e) {
            tx.rollback(); // Rollback on failure
            logger.error("Error deleting user by id: " + e.getMessage());
            throw new ServiceException("An unexpected error occurred while deleting the user");
        }
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email address of the user.
     * @return The user object or null if not found.
     * @throws ServiceException If an error occurs during retrieval.
     */
    public static User getUserByEmail(String email) throws ServiceException {
        EntityManager em = Database.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder(); // Create criteria builder
            CriteriaQuery<User> query = cb.createQuery(User.class); // Define query
            Root<User> root = query.from(User.class); // Define root entity
            query.select(root).where(cb.equal(root.get("email"), email)); // Filter by email
            return em.createQuery(query).getSingleResult(); // Execute query and return single result
        } catch (NoResultException e) {
            return null; // Return null if no result found
        } catch (Exception e) {
            logger.error("Error getting user by email: " + e.getMessage());
            throw new ServiceException("An unexpected error occurred while retrieving the user");
        }
    }

    /**
     * Updates the settings of a user.
     *
     * @param ctx      The HTTP context.
     * @param id       The UUID of the user.
     * @param settings The updated settings.
     * @throws ServiceException If an error occurs during update.
     */
    public static void updateSettings(Context ctx, UUID id, SettingsDTO settings) throws ServiceException {
        EntityManager em = Database.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin(); // Start transaction
            User user = em.find(User.class, id); // Find user by ID
            if (user == null) {
                throw new ServiceException("User not found"); // Throw exception if user not found
            }
            if (settings.isDarkMode() != null) {
                user.setDarkMode(settings.isDarkMode()); // Update dark mode setting
            }
            if (settings.isLanguageGerman() != null) {
                user.setLanguageGerman(settings.isLanguageGerman()); // Update language setting
            }
            em.merge(user); // Merge updated user
            tx.commit(); // Commit transaction
            ctx.sessionAttribute("user", user); // Update session attribute
        } catch (Exception e) {
            tx.rollback(); // Rollback on failure
            logger.error("Error updating settings: " + e.getMessage());
            e.printStackTrace(); // Log stack trace
            throw new ServiceException("An unexpected error occurred while updating the settings");
        }
    }
}
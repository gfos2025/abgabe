package org.worksale.services;

import java.util.*;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.worksale.Database;
import org.worksale.models.appointments.*;
import org.worksale.models.customers.Customer;
import org.worksale.models.users.User;
import org.worksale.requests.CreateAppointment;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

public class AppointmentService {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    /**
     * Creates a new appointment with the given details.
     * Validates appointment details before creating the appointment in the system.
     * 
     * @param appointment_data The appointment data to be created.
     * @throws ServiceException If there is an error during appointment creation
     */
    public static void createAppointment(CreateAppointment appointment_data) throws ServiceException {
        EntityManager em = Database.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Appointment appointment = new Appointment();
            appointment.setStartTime(appointment_data.startDate);
            appointment.setEndTime(appointment_data.endDate);
            appointment.setDescription(appointment_data.description);

            em.persist(appointment);

            // Hashmap to track added users and customers to prevent duplicates
            Set<UUID> addedUsers = new HashSet<>();
            Set<UUID> addedCustomers = new HashSet<>();

            for (UUID userId : appointment_data.userIds) {
                // If user is already added, throw an error
                if (!addedUsers.add(userId)) {
                    throw new ServiceException("User with ID " + userId + " is added multiple times.");
                }

                // Find user by ID
                User user = em.find(User.class, userId);

                // If user is not found, throw an error
                if (user == null) {
                    throw new ServiceException("User with ID " + userId + " not found.");
                }

                // Create appointment participant
                AppointmentParticipant participant = new AppointmentParticipant();
                participant.setAppointment(appointment);
                participant.setUser(user);

                // Set role based on organizer ID
                if (appointment_data.organizerId.equals(userId)) {
                    participant.setRole(AppointmentParticipentRole.ORGANIZER);
                } else {
                    participant.setRole(AppointmentParticipentRole.PARTICIPANT);
                }
                em.persist(participant);
            }

            // Add customers
            for (UUID customerId : appointment_data.customerIds) {
                Customer customer = em.find(Customer.class, customerId);
                if (customer == null) {
                    throw new ServiceException("Customer with ID " + customerId + " not found.");
                }

                if (!addedCustomers.add(customerId)) {
                    throw new ServiceException("Customer with ID " + customerId + " is added multiple times.");
                }

                // Create appointment participant
                AppointmentParticipant participant = new AppointmentParticipant();
                participant.setAppointment(appointment);
                participant.setCustomer(customer);
                participant.setRole(AppointmentParticipentRole.PARTICIPANT);
                em.persist(participant); // Persist participant
            }

            tx.commit();

        } catch (ServiceException e) {
            throw e; // Rethrow ServiceException
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback(); // Rollback transaction on error
            }
            logger.error("Error creating appointment: " + e.getMessage());
            throw new ServiceException("Failed to create appointment");
        } finally {
            em.close(); // Close EntityManager
        }
    }

    /**
     * Updates an existing appointment.
     *
     * @param appointmentId   The UUID of the appointment to be updated.
     * @param appointmentData The updated appointment data.
     * @throws ServiceException If there is an error during the appointment update.
     */
    public static void updateAppointment(UUID appointmentId, CreateAppointment appointmentData)
            throws ServiceException {
        EntityManager em = Database.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Appointment existingAppointment = em.find(Appointment.class, appointmentId);
            if (existingAppointment == null) {
                throw new ServiceException("Appointment with ID " + appointmentId + " not found.");
            }

            if (appointmentData.startDate != null) {
                existingAppointment.setStartTime(appointmentData.startDate);
            }
            if (appointmentData.endDate != null) {
                existingAppointment.setEndTime(appointmentData.endDate);
            }
            if (appointmentData.description != null) {
                existingAppointment.setDescription(appointmentData.description);
            }

            if (appointmentData.userIds != null && !appointmentData.userIds.isEmpty()) {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<AppointmentParticipant> query = cb.createQuery(AppointmentParticipant.class);
                Root<AppointmentParticipant> root = query.from(AppointmentParticipant.class);
                query.select(root).where(
                        cb.and(
                                cb.isNotNull(root.get("user")),
                                cb.equal(root.get("appointment").get("id"), appointmentId)));
                List<AppointmentParticipant> participants = em.createQuery(query).getResultList();

                // Remove existing participants
                for (AppointmentParticipant participant : participants) {
                    em.remove(participant);
                }

                Set<UUID> addedUsers = new HashSet<>();

                for (UUID userId : appointmentData.userIds) {
                    if (!addedUsers.add(userId)) {
                        throw new ServiceException("User with ID " + userId + " is added multiple times.");
                    }
                    User user = em.find(User.class, userId);
                    if (user == null) {
                        throw new ServiceException("User with ID " + userId + " not found.");
                    }
                    AppointmentParticipant participant = new AppointmentParticipant();
                    participant.setAppointment(existingAppointment);
                    participant.setUser(user);
                    if (appointmentData.organizerId != null && appointmentData.organizerId.equals(userId)) {
                        participant.setRole(AppointmentParticipentRole.ORGANIZER);
                    } else {
                        participant.setRole(AppointmentParticipentRole.PARTICIPANT);
                    }
                    em.persist(participant);
                }
            }

            // Add updated participants (customers)
            if (appointmentData.customerIds != null && !appointmentData.customerIds.isEmpty()) {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<AppointmentParticipant> query = cb.createQuery(AppointmentParticipant.class);
                Root<AppointmentParticipant> root = query.from(AppointmentParticipant.class);
                query.select(root).where(
                        cb.and(
                                cb.isNotNull(root.get("customer")),
                                cb.equal(root.get("appointment").get("id"), appointmentId)));
                List<AppointmentParticipant> participants = em.createQuery(query).getResultList();

                // Remove existing participants
                for (AppointmentParticipant participant : participants) {
                    em.remove(participant);
                }

                Set<UUID> addedCustomers = new HashSet<>();

                for (UUID customerId : appointmentData.customerIds) {
                    if (!addedCustomers.add(customerId)) {
                        throw new ServiceException("Customer with ID " + customerId + " is added multiple times.");
                    }

                    Customer customer = em.find(Customer.class, customerId);
                    if (customer == null) {
                        throw new ServiceException("Customer with ID " + customerId + " not found.");
                    }
                    AppointmentParticipant participant = new AppointmentParticipant();
                    participant.setAppointment(existingAppointment);
                    participant.setCustomer(customer);
                    participant.setRole(AppointmentParticipentRole.PARTICIPANT);
                    em.persist(participant);
                }
            }

            tx.commit();
        } catch (ServiceException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            logger.error("Error updating appointment: " + e.getMessage());
            throw new ServiceException("Failed to update appointment");
        } finally {
            em.close();
        }
    }

    /**
     * Converts a list of Appointment entities to a list of AppointmentDTOs.
     * Each AppointmentDTO includes a list of AppointmentParticipantDTOs
     * representing the participants of the appointment.
     *
     * @param appointments The list of Appointment entities to be converted.
     * @return A list of AppointmentDTOs with participant details.
     */

    public static List<AppointmentDTO> mapAppointmentsToDto(List<Appointment> appointments) {
        return appointments.stream().map(appointment -> {
            // Map participants to AppointmentParticipantDTO
            List<AppointmentParticipantDTO> participantDtos = appointment.getParticipants().stream()
                    .map(participant -> {
                        UUID userId = null;
                        UUID customerId = null;
                        String name;

                        if (participant.getUser() != null) {
                            userId = participant.getUser().getId();
                            name = participant.getUser().getName();
                        } else {
                            customerId = participant.getCustomer().getId();
                            name = participant.getCustomer().getName();
                        }

                        return new AppointmentParticipantDTO(
                                participant.getId(),
                                name,
                                userId,
                                customerId,
                                participant.getRole());
                    })
                    .toList();

            return new AppointmentDTO(
                    appointment.getId(),
                    appointment.getStartTime(),
                    appointment.getEndTime(),
                    appointment.getDescription(),
                    participantDtos);
        }).toList();
    }

    /**
     * Retrieves all appointments for a specific user by their unique ID.
     * Maps the list of Appointment entities to AppointmentDTOs including
     * participant details.
     *
     * @param id The UUID of the user for whom to retrieve appointments.
     * @return A list of AppointmentDTOs corresponding to the user's appointments.
     *         Returns an empty list if an error occurs during retrieval.
     */

    public static List<AppointmentDTO> getUserAppointments(UUID id) throws ServiceException {
        EntityManager em = Database.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Appointment> query = cb.createQuery(Appointment.class);
            Root<AppointmentParticipant> participantRoot = query.from(AppointmentParticipant.class);
            Join<AppointmentParticipant, Appointment> appointmentJoin = participantRoot.join("appointment");

            Predicate pred = cb.equal(participantRoot.get("user").get("id"), id);

            query.select(appointmentJoin).where(pred);
            List<Appointment> appointments = em.createQuery(query).getResultList();
            return mapAppointmentsToDto(appointments);

        } catch (Exception e) {
            logger.error("Error retrieving user appointments: " + e.getMessage(), e);
            throw new ServiceException("An unexpected error occurred while retrieving user appointments");
        } finally {
            em.close();
        }
    }

    /**
     * Retrieves all appointments for a specific customer by their unique ID.
     * Maps the list of Appointment entities to AppointmentDTOs including
     * participant details.
     *
     * @param id The UUID of the customer for whom to retrieve appointments.
     * @return A list of AppointmentDTOs corresponding to the customer's
     *         appointments.
     *         Returns an empty list if an error occurs during retrieval.
     */
    public static List<AppointmentDTO> getCustomerAppointments(UUID id) throws ServiceException {
        EntityManager em = Database.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Appointment> query = cb.createQuery(Appointment.class);
            Root<AppointmentParticipant> participantRoot = query.from(AppointmentParticipant.class);
            Join<AppointmentParticipant, Appointment> appointmentJoin = participantRoot.join("appointment");

            Predicate pred = cb.equal(participantRoot.get("customer").get("id"), id);

            query.select(appointmentJoin).where(pred);
            List<Appointment> appointments = em.createQuery(query).getResultList();
            return mapAppointmentsToDto(appointments);

        } catch (Exception e) {
            logger.error("Error retrieving customer appointments: " + e.getMessage(), e);
            throw new ServiceException("An unexpected error occurred while retrieving customer appointments");
        } finally {
            em.close();
        }
    }

    /**
     * Retrieves an appointment by its ID.
     *
     * @param id The UUID of the appointment to retrieve.
     * @return The Appointment object if found, otherwise null.
     * @throws ServiceException If an error occurs during retrieval.
     */
    public static Appointment getAppointment(UUID id) throws ServiceException {
        EntityManager em = Database.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Appointment> query = cb.createQuery(Appointment.class);
            Root<Appointment> root = query.from(Appointment.class);
            root.fetch("participants", JoinType.LEFT);

            Predicate pred = cb.equal(root.get("id"), id);

            query.select(root).where(pred);
            return em.createQuery(query).getSingleResult();
        } catch (NoResultException e) {
            return null; // Return null if no result found
        } catch (Exception e) {
            logger.error("Error retrieving appointment: " + e.getMessage());
            throw new ServiceException("An unexpected error occurred while retrieving the appointment");
        } finally {
            em.close();
        }
    }

    /**
     * Deletes an appointment by its ID.
     *
     * @param id The UUID of the appointment to delete.
     * @throws ServiceException If the appointment is not found or an error occurs
     *                          during deletion.
     */
    public static void deleteAppointment(UUID id) throws ServiceException {
        EntityManager em = Database.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Appointment appointment = em.find(Appointment.class, id);
            em.remove(appointment);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            logger.error("Error deleting appointment: " + e.getMessage());
            throw new ServiceException("An unexpected error occurred while deleting the appointment");
        } finally {
            em.close();
        }
    }
}

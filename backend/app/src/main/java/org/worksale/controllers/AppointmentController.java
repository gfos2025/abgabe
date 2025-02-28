package org.worksale.controllers;

import java.time.LocalDateTime;
import java.util.UUID;

import org.worksale.models.LogType;
import org.worksale.models.Role;
import org.worksale.models.appointments.*;
import org.worksale.models.users.User;
import org.worksale.requests.CreateAppointment;
import org.worksale.services.*;
import io.javalin.http.Context;

/**
 * Controller responsible for handling appointment-related HTTP requests.
 * This class provides endpoints for creating, updating, and deleting
 * appointments.
 */
public class AppointmentController {

        /**
         * Creates a new appointment based on the request data.
         * Validates appointment details including start/end dates, participants,
         * and description before creating the appointment in the system.
         *
         * @param ctx The Javalin context containing the request data and session
         *            information
         * @throws ServiceException If there is an error during appointment creation
         */
        public void createAppointment(Context ctx) throws ServiceException {
                // Get the user from the session
                User ctxUser = ctx.sessionAttribute("user");

                // Validate the appointment data with multiple business rule checks
                CreateAppointment data = ctx.bodyValidator(CreateAppointment.class)
                                // Date validation checks
                                .check(appointment -> appointment.startDate != null, "Start time is required")
                                .check(appointment -> appointment.startDate.isAfter(LocalDateTime.now()),
                                                "Start time must be in the future")

                                .check(appointment -> appointment.endDate != null, "End time is required")
                                .check(appointment -> appointment.endDate.isAfter(LocalDateTime.now()),
                                                "End time must be in the future")
                                .check(appointment -> appointment.endDate.isAfter(appointment.startDate),
                                                "End time must be after start time")

                                // Participant validation checks
                                .check(appointment -> appointment.userIds != null && !appointment.userIds.isEmpty(),
                                                "At least one user ID must be provided")

                                .check(appointment -> appointment.customerIds != null
                                                && !appointment.customerIds.isEmpty(),
                                                "At least one customer ID must be provided")

                                // Organizer validation
                                .check(appointment -> appointment.organizerId != null, "Organizer ID is required")
                                .check(appointment -> appointment.userIds.contains(appointment.organizerId),
                                                "Organizer ID must be part of the user participants")

                                // Content validation
                                .check(appointment -> appointment.description != null
                                                && !appointment.description.isBlank(),
                                                "Description cannot be blank")
                                .get();

                // Create the appointment using the service layer
                AppointmentService.createAppointment(data);

                // Log the appointment creation action
                LogService.createEntry(
                                String.format("User %s %s created a new appointment", ctxUser.getName(),
                                                ctxUser.getSurname()),
                                LogType.CREATED_APPOINTMENT, ctxUser.getId());

                // Return success response with 201 Created status
                ctx.status(201).result("Appointment successfully created");
        }

        /**
         * Updates an existing appointment identified by the ID in the request path.
         * Validates that any updated fields meet the system requirements.
         * Partial updates are supported - only provided fields will be updated.
         *
         * @param ctx The Javalin context containing the request data and session
         *            information
         * @throws ServiceException If there is an error during appointment update
         */
        public void updateAppointment(Context ctx) throws ServiceException {
                // Get the user from the session
                User ctxUser = ctx.sessionAttribute("user");

                // Get the appointment ID from the path parameter
                String id = ctx.pathParam("id");

                // Validate the update data - note these are less strict than creation
                // to allow for partial updates
                CreateAppointment data = ctx.bodyValidator(CreateAppointment.class)
                                // Optional date validations (fields may be null for partial updates)
                                .check(appointment -> appointment.startDate == null
                                                || appointment.startDate.isAfter(LocalDateTime.now()),
                                                "Start time must be in the future if provided")
                                .check(appointment -> appointment.endDate == null
                                                || appointment.endDate.isAfter(LocalDateTime.now()),
                                                "End time must be in the future if provided")
                                .check(appointment -> appointment.startDate == null || appointment.endDate == null ||
                                                appointment.endDate.isAfter(appointment.startDate),
                                                "End time must be after start time if both are provided")

                                // Optional participant validations
                                .check(appointment -> appointment.userIds == null || !appointment.userIds.isEmpty(),
                                                "User IDs cannot be empty if provided")
                                .check(appointment -> appointment.customerIds == null
                                                || !appointment.customerIds.isEmpty(),
                                                "Customer IDs cannot be empty if provided")

                                // Optional organizer validations
                                .check(appointment -> appointment.organizerId == null ||
                                                (appointment.userIds != null && appointment.userIds
                                                                .contains(appointment.organizerId)),
                                                "Organizer ID must be part of the user participants if provided")
                                .check(appointment -> appointment.userIds == null ||
                                                appointment.organizerId != null,
                                                "An organizer ID must be specified when updating user participants")

                                // Optional content validation
                                .check(appointment -> appointment.description == null
                                                || !appointment.description.isBlank(),
                                                "Description cannot be blank if provided")
                                .get();

                // Update the appointment using the service layer
                AppointmentService.updateAppointment(UUID.fromString(id), data);

                // Log the update action
                LogService.createEntry(
                                String.format("User %s %s updated appointment with ID %s", ctxUser.getName(),
                                                ctxUser.getSurname(), id),
                                LogType.UPDATED_APPOINTMENT, ctxUser.getId());

                // Return success response
                ctx.status(200).result("Appointment successfully updated");
        }

        /**
         * Deletes an appointment identified by the ID in the request path.
         * Checks user permissions to ensure only appointment organizers or
         * administrators
         * can delete appointments.
         *
         * @param ctx The Javalin context containing the request data and session
         *            information
         * @throws ServiceException If there is an error during appointment deletion
         */
        public void deleteAppointment(Context ctx) throws ServiceException {
                // Get the user from the session
                User ctxUser = ctx.sessionAttribute("user");

                // Get the appointment ID from the path parameter
                String id = ctx.pathParam("id");

                Appointment appointment = AppointmentService.getAppointment(UUID.fromString(id));

                // Return 404 if appointment not found
                if (appointment == null) {
                        ctx.status(404).result("Appointment not found");
                        return;
                }

                // Check if the current user is the organizer of the appointment
                boolean is_organizer = false;

                for (AppointmentParticipant participant : appointment.getParticipants()) {
                        if (participant.getRole() == AppointmentParticipentRole.ORGANIZER
                                        && participant.getUser().getId() == ctxUser.getId()) {
                                is_organizer = true;
                                break;
                        }
                }

                // Only allow deletion if user is admin or the organizer
                if (ctxUser.getRole() != Role.ADMIN && !is_organizer) {
                        ctx.status(403).result("You are not allowed to delete this appointment");
                        return;
                }

                // Delete the appointment using the service layer
                AppointmentService.deleteAppointment(UUID.fromString(id));

                // Log the deletion action
                LogService.createEntry(
                                String.format("User %s %s deleted appointment with ID %s", ctxUser.getName(),
                                                ctxUser.getSurname(), id),
                                LogType.DELETED_APPOINTMENT, ctxUser.getId());

                ctx.status(200).result("Appointment successfully deleted");
        }
}
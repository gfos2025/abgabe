package org.worksale.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.worksale.models.LogType;
import org.worksale.models.appointments.AppointmentDTO;
import org.worksale.models.customers.Customer;
import org.worksale.models.users.User;
import org.worksale.requests.*;
import org.worksale.services.*;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

/**
 * Controller responsible for handling customer-related HTTP requests.
 * Provides endpoints for creating, reading, updating, and deleting customers.
 */
public class CustomerController {

        /**
         * Creates a new customer based on request data.
         * Validates required customer information before creation.
         *
         * @param ctx The Javalin context containing the request data and session
         *            information
         * @throws ServiceException If there is an error during customer creation
         */
        public void createCustomer(Context ctx) throws ServiceException {
                // Get the user from the session
                User ctxUser = ctx.sessionAttribute("user");

                // Validate the customer data - ensure required fields are present
                CreateCustomer body = ctx.bodyValidator(CreateCustomer.class)
                                .check(customer -> customer.name != null && customer.name.length() > 0,
                                                "Name is required")
                                .check(customer -> customer.surname != null && customer.surname.length() > 0,
                                                "Surname is required")
                                .check(customer -> customer.email != null && customer.email.length() > 0,
                                                "Email is required")
                                .check(customer -> customer.company != null && customer.company.length() > 0,
                                                "Company is required")
                                .check(customer -> customer.assignedManagerId != null,
                                                "Assigned manager ID is required")
                                .get();

                if (UserService.getUserById(body.assignedManagerId) == null) {
                        throw new NotFoundResponse("Assigned manager not found");
                }

                // Create a new customer entity and populate required fields
                Customer customer = new Customer();
                customer.setName(body.name);
                customer.setSurname(body.surname);
                customer.setCompany(body.company);
                customer.setEmail(body.email);

                // Set optional fields if present
                Optional.ofNullable(body.phoneNumber).ifPresent(customer::setPhoneNumber);
                Optional.ofNullable(body.contactStatus).ifPresent(customer::setContactStatus);
                Optional.ofNullable(body.address).ifPresent(customer::setAddress);

                // Create the customer using the service layer
                CustomerService.createCustomer(customer);

                // Log the customer creation action
                LogService.createEntry(
                                String.format("User %s %s created a new customer with email %s", ctxUser.getName(),
                                                ctxUser.getSurname(),
                                                customer.getEmail()),
                                LogType.CREATED_CUSTOMER, ctxUser.getId());

                ctx.status(201).result("Customer successfully created");
        }

        /**
         * Retrieves all customers from the system.
         *
         * @param ctx The Javalin context for the response
         * @throws ServiceException If there is an error retrieving customers
         */
        public void getCustomers(Context ctx) throws ServiceException {
                // Retrieve all customers from the service layer
                List<Customer> customers = CustomerService.getCustomers();

                ctx.status(200).json(customers);
        }

        /**
         * Retrieves a specific customer by ID.
         *
         * @param ctx The Javalin context containing the path parameter and response
         * @throws ServiceException If there is an error retrieving the customer
         * @throws NotFoundResponse If no customer is found with the specified ID
         */
        public void getCustomer(Context ctx) throws ServiceException {
                // Get customer ID from path parameter
                String id = ctx.pathParam("id");

                // Retrieve the customer from the service layer
                Customer customer = CustomerService.getCustomerById(UUID.fromString(id));

                // Throw a not found response if customer doesn't exist
                if (customer == null) {
                        throw new NotFoundResponse("Customer not found");
                }

                ctx.status(200).json(customer);
        }

        /**
         * Updates an existing customer identified by ID.
         * Only updates fields that are provided in the request.
         *
         * @param ctx The Javalin context containing the path parameter and request data
         * @throws ServiceException If there is an error updating the customer
         */
        public void updateCustomer(Context ctx) throws ServiceException {
                // Get the user from the session
                User ctxUser = ctx.sessionAttribute("user");

                // Get customer ID from path parameter
                String id = ctx.pathParam("id");

                // Get update data from request body
                UpdateCustomer body = ctx.bodyValidator(UpdateCustomer.class)
                                .check(customer -> customer.name == null || !customer.name.isBlank(),
                                                "Name cannot be blank if provided")
                                .check(customer -> customer.surname == null || !customer.surname.isBlank(),
                                                "Surname cannot be blank if provided")
                                .check(customer -> customer.email == null || !customer.email.isBlank(),
                                                "Email cannot be blank if provided")
                                .check(customer -> customer.company == null || !customer.company.isBlank(),
                                                "Company cannot be blank if provided")
                                .get();

                // Update the customer using the service layer
                CustomerService.updateCustomer(UUID.fromString(id), body);

                // Log the update action
                LogService.createEntry(
                                String.format("User %s %s updated customer with ID %s", ctxUser.getName(),
                                                ctxUser.getSurname(), id),
                                LogType.UPDATED_CUSTOMER, ctxUser.getId());

                ctx.status(200).result("Customer successfully updated");
        }

        /**
         * Deletes a customer identified by ID.
         *
         * @param ctx The Javalin context containing the path parameter
         * @throws ServiceException If there is an error deleting the customer
         */
        public void deleteCustomer(Context ctx) throws ServiceException {
                // Get the user from the session
                User ctxUser = ctx.sessionAttribute("user");

                // Get customer ID from path parameter
                String id = ctx.pathParam("id");

                // Delete the customer using the service layer
                CustomerService.deleteCustomer(UUID.fromString(id));

                // Log the deletion action
                LogService.createEntry(
                                String.format("User %s %s deleted customer with ID %s", ctxUser.getName(),
                                                ctxUser.getSurname(), id),
                                LogType.DELETED_CUSTOMER, ctxUser.getId());

                ctx.status(200).result("Customer successfully deleted");
        }

        /**
         * Retrieves all appointments for a specific customer by their unique ID.
         *
         * @param ctx The Javalin context containing the path parameter and response.
         * @throws ServiceException If an error occurs during the appointment retrieval.
         */
        public void getCustomerAppointments(Context ctx) throws ServiceException {
                // Get customer ID from path parameter
                String id = ctx.pathParam("id");

                // Retrieve the customer's appointments using the service layer
                List<AppointmentDTO> appointments = AppointmentService.getCustomerAppointments(UUID.fromString(id));

                ctx.status(200).json(appointments);
        }
}
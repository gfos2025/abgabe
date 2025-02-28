package org.worksale.controllers;

import java.util.*;
import java.util.stream.Collectors;

import org.worksale.models.LogType;
import org.worksale.models.users.*;
import org.worksale.requests.*;
import org.worksale.services.*;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import io.javalin.http.*;

/**
 * Controller for managing user-related operations such as login, registration,
 * retrieving users, and updating settings.
 */
public class UserController {
    private Argon2 argon2;

    /**
     * Constructor to initialize the Argon2 password hashing library.
     */
    public UserController() {
        argon2 = Argon2Factory.create();
    }

    /**
     * Handles user login by validating credentials and setting session attributes.
     *
     * @param ctx HTTP context containing request and response data.
     * @throws ServiceException if an error occurs during service execution.
     */
    public void login(Context ctx) throws ServiceException {
        // Validate login data
        Login data = ctx.bodyValidator(Login.class)
                .check(login -> login.email != null && login.email.length() > 0, "Email is required")
                .check(login -> login.password != null && login.password.length() > 0, "Password is required")
                .get();

        // Authenticate user
        User user = UserService.getUserByEmail(data.email);
        if (user == null || !argon2.verify(user.getPasswordHash(), data.password.toCharArray())) {
            throw new UnauthorizedResponse("Invalid email or password");
        }

        // Set session attribute and return user DTO
        ctx.sessionAttribute("user", user);
        ctx.status(200).json(new UserDTO(user));
    }

    /**
     * Handles user registration by creating a new user with validated data.
     *
     * @param ctx HTTP context containing request and response data.
     * @throws ServiceException if an error occurs during service execution.
     */
    public void register(Context ctx) throws ServiceException {
        User ctxUser = ctx.sessionAttribute("user");

        // Validate registration data
        Register data = ctx.bodyValidator(Register.class)
                .check(register -> register.email != null && register.email.length() > 0, "Email is required")
                .check(register -> register.password != null && register.password.length() > 0, "Password is required")
                .check(register -> register.name != null && register.name.length() > 0, "Name is required")
                .check(register -> register.surname != null && register.surname.length() > 0, "Surname is required")
                .check(register -> register.role != null, "Role is required")
                .get();

        // Ensure user cannot create higher-role users
        if (data.role.ordinal() > ctxUser.getRole().ordinal()) {
            throw new BadRequestResponse("You cannot create a user with a higher role than your own");
        }

        // Create new user
        UserService.createUser(data);

        // Log registration activity
        LogService.createEntry(String.format("User %s %s created a new user with email %s", ctxUser.getName(),
                ctxUser.getSurname(), data.email), LogType.REGISTERED_USER, ctxUser.getId());

        ctx.status(201).result("User successfully created");
    }

    /**
     * Retrieves all users and returns them as a list of DTOs.
     *
     * @param ctx HTTP context containing request and response data.
     */
    public void getUsers(Context ctx) throws ServiceException {
        // Retrieve all users and convert to DTOs
        List<User> users = UserService.getAllUsers();
        List<UserDTO> usersDTO = users.stream().map(UserDTO::new).collect(Collectors.toList());
        ctx.json(usersDTO);
    }

    /**
     * Retrieves a specific user by ID and ensures proper access control.
     *
     * @param ctx HTTP context containing request and response data.
     */
    public void getUser(Context ctx) {
        String id = ctx.pathParam("id");
        User ctxUser = ctx.sessionAttribute("user");
        User user = UserService.getUserById(UUID.fromString(id));

        // Check if user exists and enforce role-based access control
        if (user == null) {
            throw new NotFoundResponse("User not found");
        }
        if (user.getRole().ordinal() > ctxUser.getRole().ordinal()) {
            throw new BadRequestResponse("You cannot view a user with a higher role than your own");
        }

        ctx.json(new UserDTO(user));
    }

    /**
     * Retrieves all employees from the system.
     *
     * @param ctx HTTP context containing request and response data.
     */
    public void getEmployees(Context ctx) throws ServiceException {
        ctx.json(UserService.getEmployees());
    }

    /**
     * Retrieves all appointments for a specific user.
     *
     * @param ctx HTTP context containing request and response data.
     */
    public void getAppointments(Context ctx) {
        String id = ctx.pathParam("id");
        ctx.json(AppointmentService.getUserAppointments(UUID.fromString(id)));
    }

    /**
     * Retrieves the current user's settings.
     *
     * @param ctx HTTP context containing request and response data.
     */
    public void getSettings(Context ctx) {
        User ctxUser = ctx.sessionAttribute("user");
        ctx.json(new SettingsDTO(ctxUser.isDarkMode(), ctxUser.isLanguageGerman()));
    }

    /**
     * Updates the current user's settings.
     *
     * @param ctx HTTP context containing request and response data.
     * @throws ServiceException if an error occurs during service execution.
     */
    public void updateSettings(Context ctx) throws ServiceException {
        User ctxUser = ctx.sessionAttribute("user");
        SettingsDTO settings = ctx.bodyAsClass(SettingsDTO.class);

        // Update user settings in the database
        UserService.updateSettings(ctx, ctxUser.getId(), settings);
        ctx.status(200).result("Settings successfully updated");
    }
}
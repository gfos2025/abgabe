package org.worksale;

import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.Javalin;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.UnauthorizedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.worksale.controllers.*;
import org.worksale.models.Role;
import org.worksale.models.users.User;
import org.worksale.services.ServiceException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.core.JsonParseException;

/**
 * The main application entry point responsible for initializing the server,
 * configuring routes, and handling exceptions.
 */
public class App {
    /**
     * Environment variables loaded from the .env file.
     */
    public static Dotenv ENV = null;

    /**
     * Logger instance for logging application events.
     */
    public static final Logger logger = LoggerFactory.getLogger("main");

    public static void main(String[] args) {
        // Log application startup
        logger.info("Hello, World!");

        try {
            // Load environment variables from the .env file
            ENV = Dotenv.configure().directory("../").load();
        } catch (Exception e) {
            logger.error("Error loading .env file: " + e.getMessage());
            return;
        }

        // Initialize the database and controllers
        Database db = new Database();
        UserController userController = new UserController();
        CustomerController customerController = new CustomerController();
        TransactionController transactionController = new TransactionController();
        AppointmentController appointmentController = new AppointmentController();
        LogController logController = new LogController();

        // Create and configure the Javalin server
        Javalin.create(config -> {
            // Configure Jetty session handler for managing sessions
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(db.sessionHandler()));

            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.anyHost(); // Allow requests from any host
                    it.allowCredentials = true; // Allow credentials (e.g., cookies)
                });
            });

            // Add a global route filter for role-based authorization
            config.router.mount(router -> {
                router.beforeMatched(ctx -> {
                    User user = ctx.sessionAttribute("user"); // Get the current user from the session
                    Role role = Role.ANY; // Default role if no user is logged in

                    // Determine the actual role of the user
                    if (user != null) {
                        role = user.getRole();
                    }

                    // Check if the user's role matches the required role for the route
                    if (!ctx.routeRoles().contains(role)) {
                        throw new UnauthorizedResponse("You are not authorized to access this resource");
                    }
                });
            });
        })
                .post("/api/v1/auth/register", ctx -> userController.register(ctx), Role.ADMIN)
                .post("/api/v1/auth/login", ctx -> userController.login(ctx), Role.ANY, Role.CUSTOMER, Role.EMPLOYEE,
                        Role.ADMIN)
                .get("/api/v1/users/", ctx -> userController.getUsers(ctx), Role.ADMIN)
                .get("/api/v1/users/employees", ctx -> userController.getEmployees(ctx), Role.ADMIN, Role.EMPLOYEE)
                .get("/api/v1/users/{id}/appointments", ctx -> userController.getAppointments(ctx), Role.ADMIN,
                        Role.EMPLOYEE)
                .get("/api/v1/users/settings", ctx -> userController.getSettings(ctx), Role.ADMIN, Role.EMPLOYEE)
                .patch("/api/v1/users/settings", ctx -> userController.updateSettings(ctx), Role.ADMIN, Role.EMPLOYEE)
                .get("/api/v1/users/{id}", ctx -> userController.getUser(ctx), Role.ADMIN)
                .get("/api/v1/customers/", ctx -> customerController.getCustomers(ctx), Role.ADMIN, Role.EMPLOYEE)
                .get("/api/v1/customers/{id}", ctx -> customerController.getCustomer(ctx), Role.ADMIN, Role.EMPLOYEE)
                .post("/api/v1/customers/", ctx -> customerController.createCustomer(ctx), Role.ADMIN, Role.EMPLOYEE)
                .get("/api/v1/customers/{id}/appointments", ctx -> customerController.getCustomerAppointments(ctx),
                        Role.ADMIN, Role.EMPLOYEE)
                .patch("/api/v1/customers/{id}", ctx -> customerController.updateCustomer(ctx), Role.ADMIN,
                        Role.EMPLOYEE)
                .delete("/api/v1/customers/{id}", ctx -> customerController.deleteCustomer(ctx), Role.ADMIN,
                        Role.EMPLOYEE)
                .post("/api/v1/transactions/report", ctx -> transactionController.generateReport(ctx), Role.ADMIN,
                        Role.EMPLOYEE)
                .post("/api/v1/transactions/", ctx -> transactionController.createTransaction(ctx), Role.ADMIN,
                        Role.EMPLOYEE)
                .get("/api/v1/transactions/", ctx -> transactionController.getTransactions(ctx), Role.ADMIN,
                        Role.EMPLOYEE)
                .get("/api/v1/transactions/{id}", ctx -> transactionController.getTransaction(ctx), Role.ADMIN,
                        Role.EMPLOYEE)
                .patch("/api/v1/transactions/{id}", ctx -> transactionController.updateTransaction(ctx), Role.ADMIN,
                        Role.EMPLOYEE)
                .post("/api/v1/appointments", ctx -> appointmentController.createAppointment(ctx), Role.ADMIN,
                        Role.EMPLOYEE)
                .patch("/api/v1/appointments/{id}", ctx -> appointmentController.updateAppointment(ctx), Role.ADMIN,
                        Role.EMPLOYEE)
                .delete("/api/v1/appointments/{id}", ctx -> appointmentController.deleteAppointment(ctx), Role.ADMIN,
                        Role.EMPLOYEE)
                .post("/api/v1/logs", ctx -> logController.getLogs(ctx), Role.ADMIN)

                // Start the server on the specified port (default: 8888)
                .start(Integer.parseInt(ENV.get("PORT", "8888")))

                // Handle exceptions globally
                .exception(Exception.class, (e, ctx) -> {
                    logger.error("Internal server error: " + e.getMessage());
                    e.printStackTrace();
                    throw new InternalServerErrorResponse("An unexpected error occurred");
                })
                .exception(ServiceException.class, (e, ctx) -> {
                    throw new InternalServerErrorResponse(e.getMessage());
                })
                .exception(JsonParseException.class, (e, ctx) -> {
                    throw new InternalServerErrorResponse("Invalid JSON body");
                })
                .exception(MismatchedInputException.class, (e, ctx) -> {
                    throw new InternalServerErrorResponse("Invalid request body");
                })
                .exception(IllegalArgumentException.class, (e, ctx) -> {
                    throw new InternalServerErrorResponse("Invalid request parameters/body");
                });
    }
}

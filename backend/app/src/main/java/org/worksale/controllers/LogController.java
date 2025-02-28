package org.worksale.controllers;

import org.worksale.requests.GetLogs;
import org.worksale.services.LogService;
import org.worksale.services.ServiceException;
import io.javalin.http.Context;

/**
 * Controller for handling log-related requests.
 */
public class LogController {

    /**
     * Retrieves logs from the system based on the provided criteria.
     *
     * @param ctx The Javalin context containing the request data and response.
     * @throws ServiceException If an error occurs while retrieving the logs.
     */
    public void getLogs(Context ctx) throws ServiceException {
        // Parse the request body into a GetLogs object
        GetLogs logCriteria = ctx.bodyAsClass(GetLogs.class);

        // Retrieve logs using the LogService and return as JSON response
        ctx.json(LogService.getLogs(logCriteria));
    }
}
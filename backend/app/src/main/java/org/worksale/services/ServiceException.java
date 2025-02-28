package org.worksale.services;

/**
 * ServiceException
 * 
 * Exception wrapper for service layer, all exception messages are assumed to be
 * user friendly.
 */
public class ServiceException extends Exception {
    public ServiceException(String message) {
        super(message);
    }

    // This could be improved by passing the HTTP status code as well

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

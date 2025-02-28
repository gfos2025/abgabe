package org.worksale;

import javax.sql.DataSource;
import org.eclipse.jetty.server.session.*;
import jakarta.persistence.*;

/**
 * Handles database operations, including EntityManagerFactory creation and
 * session handling.
 */
public class Database {

    /**
     * The EntityManagerFactory instance created from the persistence unit defined
     * in persistence.xml.
     */
    private static EntityManagerFactory entityManagerFactory;

    /**
     * Retrieves the EntityManagerFactory instance initialized from the persistence
     * unit.
     *
     * @return EntityManagerFactory instance.
     */
    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    /**
     * Constructor to initialize the EntityManagerFactory.
     */
    public Database() {
        try {
            // Create the EntityManagerFactory using the persistence unit name
            entityManagerFactory = Persistence.createEntityManagerFactory("org.worksale");
        } catch (Exception e) {
            throw new RuntimeException("Error creating EntityManagerFactory: " + e.getMessage(), e);
        }
    }

    /**
     * Configures and returns a Jetty SessionHandler for managing sessions with
     * JDBC-based storage.
     *
     * @return Configured SessionHandler instance.
     */
    public SessionHandler sessionHandler() {
        JDBCSessionDataStoreFactory sessionDataStoreFactory = new JDBCSessionDataStoreFactory();

        // Retrieve the DataSource from Hibernate configuration
        DataSource src = (DataSource) entityManagerFactory.getProperties()
                .get("hibernate.connection.datasource");

        // Configure the DatabaseAdaptor for JDBC session storage
        DatabaseAdaptor adaptor = new DatabaseAdaptor();
        adaptor.setDriverInfo("org.postgresql.Driver", App.ENV.get("DATABASE_URL")); // Set driver and connection URL
        adaptor.setDatasource(src); // Bind the DataSource

        sessionDataStoreFactory.setDatabaseAdaptor(adaptor);

        // Create the SessionHandler and configure its cache and data store
        SessionHandler sessionHandler = new SessionHandler();
        SessionCache sessionCache = new DefaultSessionCache(sessionHandler);
        sessionCache.setSessionDataStore(sessionDataStoreFactory.getSessionDataStore(sessionHandler));

        // Enable secure session cookies
        sessionHandler.setSecureRequestOnly(true);

        // Assign the configured cache to the SessionHandler
        sessionHandler.setSessionCache(sessionCache);

        return sessionHandler;
    }
}
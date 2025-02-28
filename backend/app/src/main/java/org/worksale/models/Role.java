package org.worksale.models;

import java.io.Serializable;

import io.javalin.security.RouteRole;

public enum Role implements RouteRole {
    ANY,
    CUSTOMER,
    EMPLOYEE,
    ADMIN
}

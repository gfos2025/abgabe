package org.worksale.requests;

import java.util.UUID;

import org.worksale.models.customers.ContactStatus;

public class CreateCustomer {
    public String name;
    public String surname;
    public String email;
    public String phoneNumber;
    public String address;
    public String company;
    public ContactStatus contactStatus;
    public UUID assignedManagerId;
}

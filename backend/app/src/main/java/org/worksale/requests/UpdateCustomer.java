package org.worksale.requests;

import org.worksale.models.customers.ContactStatus;

public class UpdateCustomer {
    public String name;
    public String surname;
    public String email;
    public String phoneNumber;
    public String address;
    public String company;
    public ContactStatus contactStatus;
    public String assignedManagerId;
}

package org.worksale.models.customers;

import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.util.UUID;
import org.worksale.models.users.User;

@StaticMetamodel(Customer.class)
public abstract class Customer_ {

	public static final String PHONE_NUMBER = "phoneNumber";
	public static final String ADDRESS = "address";
	public static final String NOTES = "notes";
	public static final String CONTACT_STATUS = "contactStatus";
	public static final String SURNAME = "surname";
	public static final String ASSIGNED_MANAGER = "assignedManager";
	public static final String NAME = "name";
	public static final String COMPANY = "company";
	public static final String ID = "id";
	public static final String EMAIL = "email";

	
	/**
	 * @see org.worksale.models.customers.Customer#phoneNumber
	 **/
	public static volatile SingularAttribute<Customer, String> phoneNumber;
	
	/**
	 * @see org.worksale.models.customers.Customer#address
	 **/
	public static volatile SingularAttribute<Customer, String> address;
	
	/**
	 * @see org.worksale.models.customers.Customer#notes
	 **/
	public static volatile ListAttribute<Customer, CustomerNotes> notes;
	
	/**
	 * @see org.worksale.models.customers.Customer#contactStatus
	 **/
	public static volatile SingularAttribute<Customer, ContactStatus> contactStatus;
	
	/**
	 * @see org.worksale.models.customers.Customer#surname
	 **/
	public static volatile SingularAttribute<Customer, String> surname;
	
	/**
	 * @see org.worksale.models.customers.Customer#assignedManager
	 **/
	public static volatile SingularAttribute<Customer, User> assignedManager;
	
	/**
	 * @see org.worksale.models.customers.Customer#name
	 **/
	public static volatile SingularAttribute<Customer, String> name;
	
	/**
	 * @see org.worksale.models.customers.Customer#company
	 **/
	public static volatile SingularAttribute<Customer, String> company;
	
	/**
	 * @see org.worksale.models.customers.Customer#id
	 **/
	public static volatile SingularAttribute<Customer, UUID> id;
	
	/**
	 * @see org.worksale.models.customers.Customer
	 **/
	public static volatile EntityType<Customer> class_;
	
	/**
	 * @see org.worksale.models.customers.Customer#email
	 **/
	public static volatile SingularAttribute<Customer, String> email;

}


package org.worksale.models.customers;

import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.util.UUID;
import org.worksale.models.users.User;

@StaticMetamodel(CustomerNotes.class)
public abstract class CustomerNotes_ {

	public static final String NOTE = "note";
	public static final String AUTHOR = "author";
	public static final String ID = "id";
	public static final String CUSTOMER = "customer";

	
	/**
	 * @see org.worksale.models.customers.CustomerNotes#note
	 **/
	public static volatile SingularAttribute<CustomerNotes, String> note;
	
	/**
	 * @see org.worksale.models.customers.CustomerNotes#author
	 **/
	public static volatile SingularAttribute<CustomerNotes, User> author;
	
	/**
	 * @see org.worksale.models.customers.CustomerNotes#id
	 **/
	public static volatile SingularAttribute<CustomerNotes, UUID> id;
	
	/**
	 * @see org.worksale.models.customers.CustomerNotes
	 **/
	public static volatile EntityType<CustomerNotes> class_;
	
	/**
	 * @see org.worksale.models.customers.CustomerNotes#customer
	 **/
	public static volatile SingularAttribute<CustomerNotes, Customer> customer;

}


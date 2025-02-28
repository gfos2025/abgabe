package org.worksale.models.transactions;

import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;
import java.util.UUID;
import org.worksale.models.customers.Customer;
import org.worksale.models.users.User;

@StaticMetamodel(Transaction.class)
public abstract class Transaction_ {

	public static final String AMOUNT = "amount";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String USER = "user";
	public static final String CUSTOMER = "customer";
	public static final String TIMESTAMP = "timestamp";
	public static final String STATUS = "status";

	
	/**
	 * @see org.worksale.models.transactions.Transaction#amount
	 **/
	public static volatile SingularAttribute<Transaction, Double> amount;
	
	/**
	 * @see org.worksale.models.transactions.Transaction#description
	 **/
	public static volatile SingularAttribute<Transaction, String> description;
	
	/**
	 * @see org.worksale.models.transactions.Transaction#id
	 **/
	public static volatile SingularAttribute<Transaction, UUID> id;
	
	/**
	 * @see org.worksale.models.transactions.Transaction
	 **/
	public static volatile EntityType<Transaction> class_;
	
	/**
	 * @see org.worksale.models.transactions.Transaction#user
	 **/
	public static volatile SingularAttribute<Transaction, User> user;
	
	/**
	 * @see org.worksale.models.transactions.Transaction#customer
	 **/
	public static volatile SingularAttribute<Transaction, Customer> customer;
	
	/**
	 * @see org.worksale.models.transactions.Transaction#timestamp
	 **/
	public static volatile SingularAttribute<Transaction, LocalDateTime> timestamp;
	
	/**
	 * @see org.worksale.models.transactions.Transaction#status
	 **/
	public static volatile SingularAttribute<Transaction, TransactionStatus> status;

}


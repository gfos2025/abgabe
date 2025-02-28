package org.worksale.models.appointments;

import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.util.UUID;
import org.worksale.models.customers.Customer;
import org.worksale.models.users.User;

@StaticMetamodel(AppointmentParticipant.class)
public abstract class AppointmentParticipant_ {

	public static final String ROLE = "role";
	public static final String APPOINTMENT = "appointment";
	public static final String ID = "id";
	public static final String USER = "user";
	public static final String CUSTOMER = "customer";

	
	/**
	 * @see org.worksale.models.appointments.AppointmentParticipant#role
	 **/
	public static volatile SingularAttribute<AppointmentParticipant, AppointmentParticipentRole> role;
	
	/**
	 * @see org.worksale.models.appointments.AppointmentParticipant#appointment
	 **/
	public static volatile SingularAttribute<AppointmentParticipant, Appointment> appointment;
	
	/**
	 * @see org.worksale.models.appointments.AppointmentParticipant#id
	 **/
	public static volatile SingularAttribute<AppointmentParticipant, UUID> id;
	
	/**
	 * @see org.worksale.models.appointments.AppointmentParticipant
	 **/
	public static volatile EntityType<AppointmentParticipant> class_;
	
	/**
	 * @see org.worksale.models.appointments.AppointmentParticipant#user
	 **/
	public static volatile SingularAttribute<AppointmentParticipant, User> user;
	
	/**
	 * @see org.worksale.models.appointments.AppointmentParticipant#customer
	 **/
	public static volatile SingularAttribute<AppointmentParticipant, Customer> customer;

}


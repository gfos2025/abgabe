package org.worksale.models.appointments;

import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;
import java.util.UUID;

@StaticMetamodel(Appointment.class)
public abstract class Appointment_ {

	public static final String DESCRIPTION = "description";
	public static final String START_TIME = "startTime";
	public static final String ID = "id";
	public static final String END_TIME = "endTime";
	public static final String PARTICIPANTS = "participants";

	
	/**
	 * @see org.worksale.models.appointments.Appointment#description
	 **/
	public static volatile SingularAttribute<Appointment, String> description;
	
	/**
	 * @see org.worksale.models.appointments.Appointment#startTime
	 **/
	public static volatile SingularAttribute<Appointment, LocalDateTime> startTime;
	
	/**
	 * @see org.worksale.models.appointments.Appointment#id
	 **/
	public static volatile SingularAttribute<Appointment, UUID> id;
	
	/**
	 * @see org.worksale.models.appointments.Appointment#endTime
	 **/
	public static volatile SingularAttribute<Appointment, LocalDateTime> endTime;
	
	/**
	 * @see org.worksale.models.appointments.Appointment
	 **/
	public static volatile EntityType<Appointment> class_;
	
	/**
	 * @see org.worksale.models.appointments.Appointment#participants
	 **/
	public static volatile ListAttribute<Appointment, AppointmentParticipant> participants;

}


package org.worksale.models;

import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;
import java.util.UUID;

@StaticMetamodel(Log.class)
public abstract class Log_ {

	public static final String ID = "id";
	public static final String MESSAGE = "message";
	public static final String TYPE = "type";
	public static final String USER_ID = "userId";
	public static final String TIMESTAMP = "timestamp";

	
	/**
	 * @see org.worksale.models.Log#id
	 **/
	public static volatile SingularAttribute<Log, UUID> id;
	
	/**
	 * @see org.worksale.models.Log#message
	 **/
	public static volatile SingularAttribute<Log, String> message;
	
	/**
	 * @see org.worksale.models.Log#type
	 **/
	public static volatile SingularAttribute<Log, LogType> type;
	
	/**
	 * @see org.worksale.models.Log
	 **/
	public static volatile EntityType<Log> class_;
	
	/**
	 * @see org.worksale.models.Log#userId
	 **/
	public static volatile SingularAttribute<Log, UUID> userId;
	
	/**
	 * @see org.worksale.models.Log#timestamp
	 **/
	public static volatile SingularAttribute<Log, LocalDateTime> timestamp;

}


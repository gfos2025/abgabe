package org.worksale.models.users;

import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.util.UUID;
import org.worksale.models.Role;

@StaticMetamodel(User.class)
public abstract class User_ {

	public static final String ROLE = "role";
	public static final String SURNAME = "surname";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String DARK_MODE = "darkMode";
	public static final String IS_LANGUAGE_GERMAN = "isLanguageGerman";
	public static final String EMAIL = "email";
	public static final String PASSWORD_HASH = "passwordHash";

	
	/**
	 * @see org.worksale.models.users.User#role
	 **/
	public static volatile SingularAttribute<User, Role> role;
	
	/**
	 * @see org.worksale.models.users.User#surname
	 **/
	public static volatile SingularAttribute<User, String> surname;
	
	/**
	 * @see org.worksale.models.users.User#name
	 **/
	public static volatile SingularAttribute<User, String> name;
	
	/**
	 * @see org.worksale.models.users.User#id
	 **/
	public static volatile SingularAttribute<User, UUID> id;
	
	/**
	 * @see org.worksale.models.users.User#darkMode
	 **/
	public static volatile SingularAttribute<User, Boolean> darkMode;
	
	/**
	 * @see org.worksale.models.users.User
	 **/
	public static volatile EntityType<User> class_;
	
	/**
	 * @see org.worksale.models.users.User#isLanguageGerman
	 **/
	public static volatile SingularAttribute<User, Boolean> isLanguageGerman;
	
	/**
	 * @see org.worksale.models.users.User#email
	 **/
	public static volatile SingularAttribute<User, String> email;
	
	/**
	 * @see org.worksale.models.users.User#passwordHash
	 **/
	public static volatile SingularAttribute<User, String> passwordHash;

}


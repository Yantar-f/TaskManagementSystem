package ru.effectivemobile.taskmanagementsystem.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Account.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Account_ {

	public static final String ENCODED_PASSWORD = "encodedPassword";
	public static final String ID = "id";
	public static final String EMAIL = "email";

	
	/**
	 * @see ru.effectivemobile.taskmanagementsystem.entity.Account#encodedPassword
	 **/
	public static volatile SingularAttribute<Account, String> encodedPassword;
	
	/**
	 * @see ru.effectivemobile.taskmanagementsystem.entity.Account#id
	 **/
	public static volatile SingularAttribute<Account, Long> id;
	
	/**
	 * @see ru.effectivemobile.taskmanagementsystem.entity.Account
	 **/
	public static volatile EntityType<Account> class_;
	
	/**
	 * @see ru.effectivemobile.taskmanagementsystem.entity.Account#email
	 **/
	public static volatile SingularAttribute<Account, String> email;

}


package ru.effectivemobile.taskmanagementsystem.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import ru.effectivemobile.taskmanagementsystem.entity.Task.Priority;
import ru.effectivemobile.taskmanagementsystem.entity.Task.Status;

@StaticMetamodel(Task.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Task_ {

	public static final String AUTHOR = "author";
	public static final String EXECUTOR = "executor";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String PRIORITY = "priority";
	public static final String GRAPH_TASK_FULL_GRAPH_ = "Task[full-graph]";
	public static final String HEADLINE = "headline";
	public static final String STATUS = "status";

	
	/**
	 * @see ru.effectivemobile.taskmanagementsystem.entity.Task#author
	 **/
	public static volatile SingularAttribute<Task, Account> author;
	
	/**
	 * @see ru.effectivemobile.taskmanagementsystem.entity.Task#executor
	 **/
	public static volatile SingularAttribute<Task, Account> executor;
	
	/**
	 * @see ru.effectivemobile.taskmanagementsystem.entity.Task#description
	 **/
	public static volatile SingularAttribute<Task, String> description;
	
	/**
	 * @see ru.effectivemobile.taskmanagementsystem.entity.Task#id
	 **/
	public static volatile SingularAttribute<Task, Long> id;
	
	/**
	 * @see ru.effectivemobile.taskmanagementsystem.entity.Task#priority
	 **/
	public static volatile SingularAttribute<Task, Priority> priority;
	
	/**
	 * @see ru.effectivemobile.taskmanagementsystem.entity.Task
	 **/
	public static volatile EntityType<Task> class_;
	
	/**
	 * @see ru.effectivemobile.taskmanagementsystem.entity.Task#headline
	 **/
	public static volatile SingularAttribute<Task, String> headline;
	
	/**
	 * @see ru.effectivemobile.taskmanagementsystem.entity.Task#status
	 **/
	public static volatile SingularAttribute<Task, Status> status;

}


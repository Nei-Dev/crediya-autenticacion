package com.crediya.api.constants.swagger.user;

public class UserDocApi {
	
	private UserDocApi() {}
	
	public static final String TAG_USER = "User";
	
	// Create User
	public static final String OPERATION_ID_CREATE = "createUser";
	public static final String SUMMARY_CREATE = "Create a new user in the system";
	public static final String DESCRIPTION_CREATED = "User created successfully";
	
	// Find User by Identification
	public static final String OPERATION_ID_FIND_BY_IDENTIFICATION = "findUserByIdentification";
	public static final String SUMMARY_FIND_BY_IDENTIFICATION = "Find a user by their identification number";
	public static final String DESCRIPTION_FIND_BY_IDENTIFICATION = "User found successfully";
	
}

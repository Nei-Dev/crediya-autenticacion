package com.crediya.api.contants.swagger.user;

public class UserDocApi {
	
	private UserDocApi() {}
	
	public static final String TITLE = "CrediYa Auth API";
	public static final String DESCRIPTION = "APIs for managing users, including creation and retrieval by email.";
	public static final String VERSION = "1.0.0";
	public static final String DESCRIPTION_BAD_REQUEST = "Invalid input data";
	public static final String DESCRIPTION_INTERNAL_ERROR = "Internal server error";
	
	// Create User
	public static final String SUMMARY_CREATE = "Create a new user in the system";
	public static final String DESCRIPTION_CREATED = "User created successfully";
	
	
}

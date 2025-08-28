package com.crediya.model.constants;

public class ErrorMessage {
	
	private ErrorMessage() {
	}
	
	public static final String INVALID_USER_NAME = "Username cannot be null or empty";
	public static final String INVALID_USER_LASTNAME = "Lastname cannot be null or empty";
	public static final String NULL_EMAIL = "Email cannot be null or empty";
	public static final String INVALID_EMAIL = "Email format is invalid";
	public static final String INVALID_SALARY_BASE = "Salary base must be between ".concat(String.valueOf(SalaryBaseLimits.MIN))
		.concat(" and ")
		.concat(String.valueOf(SalaryBaseLimits.MAX));
	public static final String IDENTIFICATION_NOT_BLANK = "Identification is required and cannot be blank";
	public static final String INVALID_IDENTIFICATION = "Identification is invalid";
	
	public static final String NULL_USER = "User cannot be null";
	public static final String ALREADY_EXISTS_USER = "User with the given email or identification number already exists";
	public static final String USER_NOT_FOUND = "User not found";
	
}

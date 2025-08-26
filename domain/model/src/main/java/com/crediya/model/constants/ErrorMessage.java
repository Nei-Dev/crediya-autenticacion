package com.crediya.model.constants;

public class ErrorMessage {
	
	private ErrorMessage() {
	}
	
	public static final String INVALID_USER_NAME = "Username cannot be null or empty";
	public static final String INVALID_USER_LASTNAME = "Lastname cannot be null or empty";
	public static final String NULL_EMAIL = "Email cannot be null or empty";
	public static final String INVALID_EMAIL = "Email format is invalid";
	public static final String INVALID_SALARY_BASE = String.format("Salary base must be between %d and %d", SalaryBaseLimits.MIN, SalaryBaseLimits.MAX);
	
	public static final String NULL_USER = "User cannot be null";
	public static final String ALREADY_EXISTS_USER = "User with the given email already exists";
	
}

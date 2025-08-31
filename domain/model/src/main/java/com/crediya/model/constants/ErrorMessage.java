package com.crediya.model.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessage {
	
	public final String INVALID_USER_NAME = "Username cannot be null or empty";
	public final String INVALID_USER_LASTNAME = "Lastname cannot be null or empty";
	public final String NULL_EMAIL = "Email cannot be null or empty";
	public final String INVALID_EMAIL = "Email format is invalid";
	public final String INVALID_SALARY_BASE = "Salary base must be between ".concat(String.valueOf(SalaryBaseRules.MIN))
		.concat(" and ")
		.concat(String.valueOf(SalaryBaseRules.MAX));
	public final String IDENTIFICATION_NOT_BLANK = "Identification is required and cannot be blank";
	public final String INVALID_IDENTIFICATION = "Identification is invalid";
	public final String NULL_PASSWORD = "Password cannot be null or empty";
	public final String INVALID_PASSWORD = "Password must be at least 5 characters long";
	
	public final String NULL_USER = "User cannot be null";
	public final String ALREADY_EXISTS_USER = "User with the given email or identification number already exists";
	public final String USER_NOT_FOUND = "User not found";
	
	public final String INVALID_LOGIN = "Invalid email or password";
	
}

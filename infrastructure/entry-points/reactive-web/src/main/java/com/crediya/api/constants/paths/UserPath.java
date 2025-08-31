package com.crediya.api.constants.paths;

public class UserPath {
	
	private UserPath() {}
	
	public static final String BASE_PATH = "/api/v1";
	
	public static final String USER_PATH = BASE_PATH + "/users";
	public static final String SEARCH_BY_IDENTIFICATION = USER_PATH + "/identification/{identificationNumber}";
	
}

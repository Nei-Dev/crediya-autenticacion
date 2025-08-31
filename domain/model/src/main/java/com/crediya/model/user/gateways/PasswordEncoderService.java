package com.crediya.model.user.gateways;

public interface PasswordEncoderService {
	
	String encode(String password);
	boolean matches(String rawPassword, String encodedPassword);
	
}

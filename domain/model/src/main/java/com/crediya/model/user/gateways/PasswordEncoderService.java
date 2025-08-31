package com.crediya.model.user.gateways;

public interface PasswordEncoderService {
	
	String encode(String password);
	Boolean matches(String rawPassword, String encodedPassword);
	
}

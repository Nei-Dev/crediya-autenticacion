package com.crediya.passwordencoder;

import com.crediya.model.user.gateways.PasswordEncoderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderServiceImpl implements PasswordEncoderService {
	
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public String encode(String password) {
		return passwordEncoder.encode(password);
	}
	
	@Override
	public boolean matches(String rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}
}

package com.crediya.model.user.gateways;

import com.crediya.model.user.User;
import com.crediya.model.user.UserClaims;
import reactor.core.publisher.Mono;

public interface TokenService {
	
	String generateToken(User user);
	
	Mono<UserClaims> validateToken(String token);
	
}

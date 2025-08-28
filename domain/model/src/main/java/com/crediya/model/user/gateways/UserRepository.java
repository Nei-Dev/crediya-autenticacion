package com.crediya.model.user.gateways;

import com.crediya.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
	
	Mono<User> createUser(User user);
	
	Mono<User> findByEmail(String correoElectronico);
	
	Mono<User> findByIdentification(String identification);
	
}

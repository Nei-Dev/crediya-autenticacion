package com.crediya.model.user.ports;

import com.crediya.model.user.User;
import reactor.core.publisher.Mono;

public interface ILoginUseCase {
	
	Mono<User> execute(String email, String password);
	
}

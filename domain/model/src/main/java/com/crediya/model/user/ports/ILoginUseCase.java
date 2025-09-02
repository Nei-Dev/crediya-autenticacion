package com.crediya.model.user.ports;

import reactor.core.publisher.Mono;

public interface ILoginUseCase {
	
	Mono<String> execute(String email, String password);
	
}

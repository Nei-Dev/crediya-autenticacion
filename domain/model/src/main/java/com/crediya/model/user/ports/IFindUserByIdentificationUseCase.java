package com.crediya.model.user.ports;

import com.crediya.model.user.User;
import reactor.core.publisher.Mono;

public interface IFindUserByIdentificationUseCase {
	
	Mono<User> execute(String identification);
	
}

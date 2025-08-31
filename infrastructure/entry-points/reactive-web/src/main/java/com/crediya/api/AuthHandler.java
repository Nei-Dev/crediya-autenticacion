package com.crediya.api;

import com.crediya.api.dto.input.auth.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandler {
	
	private final ValidatorApi validatorApi;
	
	public Mono<ServerResponse> login(ServerRequest serverRequest){
		return serverRequest.bodyToMono(LoginRequest.class)
			.doOnSubscribe(subscription -> log.trace("Received login request: {}", subscription))
			.flatMap(validatorApi::validate)
			.doOnSuccess(loginRequest -> log.debug("Login request validated for user"))
			.flatMap(loginRequest -> ServerResponse.ok()
				.bodyValue("Login successful for user")
			);
	}
}

package com.crediya.api.features.auth;

import com.crediya.api.dto.input.auth.LoginRequest;
import com.crediya.api.dto.output.ApiResponseDTO;
import com.crediya.api.dto.output.auth.AuthResponse;
import com.crediya.api.helpers.JwtHelper;
import com.crediya.api.helpers.ValidatorApi;
import com.crediya.api.mappers.user.UserResponseMapper;
import com.crediya.model.user.ports.ILoginUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandler {
	
	private final ValidatorApi validatorApi;
	private final ILoginUseCase loginUseCase;
	private final JwtHelper jwtHelper;
	
	public Mono<ServerResponse> login(ServerRequest serverRequest){
		return serverRequest.bodyToMono(LoginRequest.class)
			.doOnSubscribe(subscription -> log.trace("Received login request: {}", subscription))
			.flatMap(validatorApi::validate)
			.flatMap(req -> loginUseCase.execute(req.email(), req.password()))
			.map(UserResponseMapper.INSTANCE::toUserResponse)
			.doOnSuccess(user -> log.debug("Login request validated for user"))
			.flatMap(user -> ServerResponse.ok()
				.bodyValue(ApiResponseDTO.of(
					new AuthResponse(jwtHelper.generateToken(user.email(), Collections.emptyMap())),
					"Login successful"
				))
			);
	}
}

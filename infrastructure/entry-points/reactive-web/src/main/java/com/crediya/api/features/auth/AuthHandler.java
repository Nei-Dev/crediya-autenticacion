package com.crediya.api.features.auth;

import com.crediya.api.dto.input.auth.LoginRequest;
import com.crediya.api.dto.output.ApiResponse;
import com.crediya.api.dto.output.auth.AuthResponse;
import com.crediya.api.helpers.ValidatorApi;
import com.crediya.api.mappers.user.UserResponseMapper;
import com.crediya.model.user.UserClaims;
import com.crediya.model.user.ports.IFindUserByIdentificationUseCase;
import com.crediya.model.user.ports.ILoginUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.crediya.api.constants.ResponseMessage.LOGIN_SUCCESS;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandler {
	
	private final ValidatorApi validatorApi;
	private final ILoginUseCase loginUseCase;
	private final IFindUserByIdentificationUseCase findUserByIdentificationUseCase;
	
	public Mono<ServerResponse> login(ServerRequest serverRequest){
		return serverRequest.bodyToMono(LoginRequest.class)
			.doOnSubscribe(subscription -> log.trace("Received login request"))
			.flatMap(validatorApi::validate)
			.flatMap(req -> loginUseCase.execute(req.email(), req.password()))
			.doOnSuccess(token -> log.debug("Login successful, token generated"))
			.flatMap(token -> ServerResponse.ok()
				.bodyValue(ApiResponse.of(
					new AuthResponse(token),
					LOGIN_SUCCESS
				))
			);
	}
	
	public Mono<ServerResponse> findMe(ServerRequest serverRequest){
		return serverRequest.principal()
			.cast(UsernamePasswordAuthenticationToken.class)
			.map(auth -> (UserClaims) auth.getPrincipal())
			.flatMap(claims -> findUserByIdentificationUseCase.execute(claims.identification()))
			.doOnSubscribe(subscription -> log.trace("Received request to find user by token identification"))
			.doOnSuccess(user -> log.debug("User me found successfully: {}", user.getEmail()))
			.map(UserResponseMapper.INSTANCE::toUserResponse)
			.flatMap(userResponseDTO -> ServerResponse.status(HttpStatus.OK)
				.bodyValue(userResponseDTO)
			);
	}
}

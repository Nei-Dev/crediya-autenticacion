package com.crediya.api.features.user;

import com.crediya.api.constants.ResponseMessage;
import com.crediya.api.dto.input.user.CreateUserRequest;
import com.crediya.api.dto.output.user.UserApiResponse;
import com.crediya.api.helpers.ValidatorApi;
import com.crediya.api.mappers.user.UserEntityMapper;
import com.crediya.api.mappers.user.UserResponseMapper;
import com.crediya.model.exceptions.user.InvalidUserException;
import com.crediya.model.user.ports.ICreateUserClientUseCase;
import com.crediya.model.user.ports.IFindUserByIdentificationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.crediya.api.constants.ValidationMessage.IDENTIFICATION_NOT_BLANK;
import static com.crediya.model.constants.ErrorValidationMessage.NULL_USER;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {
	
	private final ValidatorApi validatorApi;
	private final ICreateUserClientUseCase createUserClientUseCase;
	private final IFindUserByIdentificationUseCase findUserByIdentificationUseCase;
	
	public Mono<ServerResponse> createUserClient(ServerRequest serverRequest){
		return serverRequest.bodyToMono(CreateUserRequest.class)
			.doOnSubscribe(subscription -> log.trace("Received request to create user"))
			.switchIfEmpty(Mono.error(new InvalidUserException(NULL_USER)))
			.flatMap(validatorApi::validate)
			.map(UserEntityMapper.INSTANCE::toUser)
			.flatMap(createUserClientUseCase::execute)
			.doOnSuccess(user -> log.debug("User created successfully: {}", user.getEmail()))
			.map(UserResponseMapper.INSTANCE::toUserResponse)
			.flatMap(userResponseDTO -> ServerResponse.status(HttpStatus.CREATED)
				.bodyValue(UserApiResponse.of(userResponseDTO,
					ResponseMessage.USER_CREATED
				))
			);
	}
	
	public Mono<ServerResponse> findUserByIdentification(ServerRequest serverRequest){
		return Mono.fromCallable(() -> serverRequest.pathVariable("identificationNumber"))
			.map(String::trim)
			.filter(item -> !item.isEmpty())
			.switchIfEmpty(Mono.error(new InvalidUserException(IDENTIFICATION_NOT_BLANK)))
			.flatMap(findUserByIdentificationUseCase::execute)
			.doOnSubscribe(subscription -> log.trace("Received request to find user by identification"))
			.doOnSuccess(user -> log.debug("User found successfully: {}", user.getEmail()))
			.map(UserResponseMapper.INSTANCE::toUserResponse)
			.flatMap(userResponseDTO -> ServerResponse.status(HttpStatus.OK)
				.bodyValue(userResponseDTO)
			);
	}
	
}

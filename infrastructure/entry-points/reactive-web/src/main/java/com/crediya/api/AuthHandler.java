package com.crediya.api;

import com.crediya.api.constants.ResponseMessage;
import com.crediya.api.dto.input.user.CreateUserRequest;
import com.crediya.api.dto.output.user.UserApiResponseDTO;
import com.crediya.api.mapper.UserEntityMapper;
import com.crediya.api.mapper.UserResponseMapper;
import com.crediya.model.exceptions.user.InvalidUserException;
import com.crediya.model.user.ports.ICreateUserClientUseCase;
import com.crediya.model.user.ports.IFindUserByIdentificationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static com.crediya.model.constants.ErrorMessage.NULL_USER;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandler {
	
	private final Validator validator;
	private final ICreateUserClientUseCase createUserClientUseCase;
	private final IFindUserByIdentificationUseCase findUserByIdentificationUseCase;
	
	public Mono<ServerResponse> createUserClient(ServerRequest serverRequest){
		return serverRequest.bodyToMono(CreateUserRequest.class)
			.doOnSubscribe(subscription -> log.trace("Received request to create user: {}", subscription))
			.switchIfEmpty(Mono.error(new InvalidUserException(NULL_USER)))
			.flatMap(this::validateData)
			.map(UserEntityMapper.INSTANCE::toUser)
			.flatMap(createUserClientUseCase::execute)
			.doOnSuccess(user -> log.debug("User created successfully: {}", user.getEmail()))
			.map(UserResponseMapper.INSTANCE::toUserResponse)
			.flatMap(userResponseDTO -> ServerResponse.status(HttpStatus.CREATED)
				.bodyValue(UserApiResponseDTO.of(userResponseDTO,
					ResponseMessage.USER_CREATED
				))
			);
	}
	
	public Mono<ServerResponse> findUserByIdentification(ServerRequest serverRequest){
		String identification = serverRequest.pathVariable("identificationNumber");
		return findUserByIdentificationUseCase.execute(identification)
			.doOnSubscribe(subscription -> log.trace("Received request to find user by identification: {}", identification))
			.doOnSuccess(user -> log.debug("User found successfully: {}", user.getEmail()))
			.map(UserResponseMapper.INSTANCE::toUserResponse)
			.flatMap(userResponseDTO -> ServerResponse.status(HttpStatus.OK)
				.bodyValue(userResponseDTO)
			);
	}
	
	private Mono<CreateUserRequest> validateData(CreateUserRequest createUserRequest) {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(createUserRequest, "createUserRequest");
		
		validator.validate(createUserRequest, errors);
		
		if (errors.hasErrors()) {
			String errorMessage = errors.getAllErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.distinct()
				.collect(Collectors.joining("; "));
			
			return Mono.error(new InvalidUserException(errorMessage));
		}
		
		return Mono.just(createUserRequest);
	}
}

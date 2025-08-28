package com.crediya.api;

import com.crediya.api.constants.ResponseMessage;
import com.crediya.api.dto.input.user.CreateUserRequest;
import com.crediya.api.dto.output.user.UserApiResponseDTO;
import com.crediya.api.dto.output.user.UserResponseDTO;
import com.crediya.api.mapper.UserEntityMapper;
import com.crediya.api.mapper.UserResponseMapper;
import com.crediya.usecase.create_user.CreateUserClientUseCase;
import com.crediya.usecase.create_user.FindUserByIdentificationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.crediya.api.constants.paths.Path.USER_PATH;
import static com.crediya.api.constants.swagger.user.UserDocApi.*;

@Slf4j
@RestController
@RequestMapping(value = USER_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController {
	
	private final CreateUserClientUseCase createUserClientUseCase;
	private final FindUserByIdentificationUseCase findUserByIdentificationUseCase;
	
	@Operation(
		summary = SUMMARY_CREATE,
		responses = {
			@ApiResponse(
				responseCode = "201",
				description = DESCRIPTION_CREATED,
				content = @Content(
					schema = @Schema(
						implementation = UserApiResponseDTO.class
					)
				)
			)
		}
	)
	@PostMapping
	public Mono<ResponseEntity<UserApiResponseDTO>> createUserClient(
		@Valid @RequestBody
		CreateUserRequest createUserRequest
	) {
		return createUserClientUseCase.execute(UserEntityMapper.INSTANCE.toUser(createUserRequest))
			.doOnSubscribe(subscription -> log.trace("Received request to create user: {}", createUserRequest))
			.doOnSuccess(user -> log.debug("User created successfully: {}", user.getEmail()))
			.map(UserResponseMapper.INSTANCE::toUserResponse)
			.map(userResponse -> ResponseEntity.status(HttpStatus.CREATED)
				.body(UserApiResponseDTO.of(userResponse, ResponseMessage.USER_CREATED)));
	}
	
	@Operation(
		summary = SUMMARY_FIND_BY_IDENTIFICATION,
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = DESCRIPTION_FOUND_BY_IDENTIFICATION,
				content = @Content(
					schema = @Schema(
						implementation = UserResponseDTO.class
					)
				)
			)
		}
	)
	@GetMapping("/identification/{identificationNumber}")
	public Mono<ResponseEntity<UserResponseDTO>> getUserByIdentificationNumber(
		@PathVariable("identificationNumber") String identificationNumber
	) {
		return findUserByIdentificationUseCase.execute(identificationNumber)
			.doOnSubscribe(subscription -> log.trace("Received request to find user by identification number: {}", identificationNumber))
			.doOnSuccess(user -> log.debug("User found successfully: {}", user.getEmail()))
			.map(UserResponseMapper.INSTANCE::toUserResponse)
			.map(userResponse -> ResponseEntity.status(HttpStatus.OK)
				.body(userResponse));
	}
}

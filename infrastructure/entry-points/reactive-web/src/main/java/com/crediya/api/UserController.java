package com.crediya.api;

import com.crediya.api.contants.ResponseMessage;
import com.crediya.api.contants.swagger.user.UserDocApi;
import com.crediya.api.dto.input.ApiResponseDTO;
import com.crediya.api.dto.input.user.CreateUserRequest;
import com.crediya.api.dto.input.user.UserApiResponse;
import com.crediya.api.dto.input.user.UserResponseDTO;
import com.crediya.api.dto.output.ErrorResponseDTO;
import com.crediya.api.mapper.UserEntityMapper;
import com.crediya.api.mapper.UserResponseMapper;
import com.crediya.usecase.create_user.CreateUserClientUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController {
	
	private final CreateUserClientUseCase crearUsuarioClienteUseCase;
	
	@Operation(
		summary = UserDocApi.SUMMARY_CREATE,
		responses = {
			@ApiResponse(
				responseCode = "201",
				description = UserDocApi.DESCRIPTION_CREATED,
				content = @Content(
					schema = @Schema(
						implementation = UserApiResponse.class
					)
				)
			),
			@ApiResponse(
				responseCode = "400",
				description = UserDocApi.DESCRIPTION_BAD_REQUEST,
				content = @Content(
					schema = @Schema(
						implementation = ErrorResponseDTO.class
					)
				)
			),
			@ApiResponse(
				responseCode = "500",
				description = UserDocApi.DESCRIPTION_INTERNAL_ERROR,
				content = @Content(
					schema = @Schema(
						implementation = ErrorResponseDTO.class
					)
				)
			)
		}
	)
	@PostMapping
	public Mono<ResponseEntity<ApiResponseDTO<UserResponseDTO>>> crearUsuarioCliente(
		@RequestBody
		CreateUserRequest createUserRequest
	) {
		return crearUsuarioClienteUseCase.execute(UserEntityMapper.INSTANCE.toUser(createUserRequest))
			.map(userCreated -> ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponseDTO.of(UserResponseMapper.INSTANCE.toUserResponse(userCreated), ResponseMessage.USER_CREATED)));
	}
}

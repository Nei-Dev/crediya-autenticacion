package com.crediya.api;

import com.crediya.api.dto.output.ErrorResponseDTO;
import com.crediya.model.exceptions.user.AlreadyExistsUserException;
import com.crediya.model.exceptions.user.InvalidRoleException;
import com.crediya.model.exceptions.user.InvalidUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public Mono<ResponseEntity<ErrorResponseDTO>> handleGenericException(Exception ex) {
		log.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
		return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ErrorResponseDTO.internalError("Ocurrió un error interno en el servidor")));
	}
	
	@ExceptionHandler(InvalidUserException.class)
	public Mono<ResponseEntity<ErrorResponseDTO>> manejarRolInvalido(InvalidUserException ex) {
		log.warn("InvalidUserException: {}", ex.getMessage());
		ErrorResponseDTO response = ErrorResponseDTO.badRequest(ex.getMessage());
		return Mono.just(ResponseEntity.status(response.getCodigo())
			.body(response));
	}
	
	@ExceptionHandler(AlreadyExistsUserException.class)
	public Mono<ResponseEntity<ErrorResponseDTO>> manejarRolInvalido(AlreadyExistsUserException ex) {
		log.warn("AlreadyExistsUserException: {}", ex.getMessage());
		ErrorResponseDTO response = ErrorResponseDTO.badRequest(ex.getMessage());
		return Mono.just(ResponseEntity.status(response.getCodigo())
			.body(response));
	}
	
	@ExceptionHandler(InvalidRoleException.class)
	public Mono<ResponseEntity<ErrorResponseDTO>> manejarRolInvalido(InvalidRoleException ex) {
		log.warn("InvalidRoleException: {}", ex.getMessage());
		ErrorResponseDTO response = ErrorResponseDTO.badRequest(ex.getMessage());
		return Mono.just(ResponseEntity.status(response.getCodigo())
			.body(response));
	}
	
}


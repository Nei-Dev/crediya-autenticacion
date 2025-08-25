package com.crediya.api;

import com.crediya.api.dto.output.ErrorResponse;
import com.crediya.model.exceptions.usuario.RolInvalidoException;
import com.crediya.model.exceptions.usuario.UsuarioInvalidoException;
import com.crediya.model.exceptions.usuario.UsuarioYaExisteException;
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
	public Mono<ResponseEntity<ErrorResponse>> manejarExcepcionGenerica(Exception ex) {
		log.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
		return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ErrorResponse.internalError("Ocurrió un error interno en el servidor")));
	}
	
	@ExceptionHandler(UsuarioInvalidoException.class)
	public Mono<ResponseEntity<ErrorResponse>> manejarRolInvalido(UsuarioInvalidoException ex) {
		log.warn("UsuarioInvalidoException: {}", ex.getMessage());
		ErrorResponse response = ErrorResponse.badRequest(ex.getMessage());
		return Mono.just(ResponseEntity.status(response.getCodigo())
			.body(response));
	}
	
	@ExceptionHandler(UsuarioYaExisteException.class)
	public Mono<ResponseEntity<ErrorResponse>> manejarRolInvalido(UsuarioYaExisteException ex) {
		log.warn("UsuarioYaExisteException: {}", ex.getMessage());
		ErrorResponse response = ErrorResponse.badRequest(ex.getMessage());
		return Mono.just(ResponseEntity.status(response.getCodigo())
			.body(response));
	}
	
	@ExceptionHandler(RolInvalidoException.class)
	public Mono<ResponseEntity<ErrorResponse>> manejarRolInvalido(RolInvalidoException ex) {
		log.warn("RolInvalidoException: {}", ex.getMessage());
		ErrorResponse response = ErrorResponse.badRequest(ex.getMessage());
		return Mono.just(ResponseEntity.status(response.getCodigo())
			.body(response));
	}
	
}


package com.crediya.api;

import com.crediya.api.dto.output.ErrorResponse;
import com.crediya.model.exceptions.BusinessException;
import com.crediya.model.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static com.crediya.api.constants.ErrorMessage.GENERIC_ERROR;
import static com.crediya.api.constants.swagger.DocApi.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	@ApiResponse(
	    responseCode = "500",
	    description = DESCRIPTION_INTERNAL_ERROR,
	    content = @Content(
			schema = @Schema(
				implementation = ErrorResponse.class
			),
	        mediaType = MediaType.APPLICATION_JSON_VALUE
	    )
	)
	public Mono<ResponseEntity<ErrorResponse>> handleGenericException(Exception ex) {
		log.error(
			"Unhandled exception occurred: {}",
			ex.getMessage(),
			ex
		);
		return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ErrorResponse.internalServerError(GENERIC_ERROR)));
	}
	
	@ExceptionHandler(BusinessException.class)
	@ApiResponse(
		responseCode = "400",
		description = DESCRIPTION_BAD_REQUEST,
		content = @Content(
			schema = @Schema(
				implementation = ErrorResponse.class
			),
			mediaType = MediaType.APPLICATION_JSON_VALUE
		)
	)
	public Mono<ResponseEntity<ErrorResponse>> handleBusinessException(BusinessException ex) {
		ErrorResponse response = ErrorResponse.badRequest(ex.getMessage());
		return Mono.just(ResponseEntity.status(response.getCode()).contentType(MediaType.APPLICATION_JSON).body(response));
	}

	@ExceptionHandler(WebExchangeBindException.class)
	@ApiResponse(
		responseCode = "400",
		description = DESCRIPTION_BAD_REQUEST,
		content = @Content(
			schema = @Schema(
				implementation = ErrorResponse.class
			),
			mediaType = MediaType.APPLICATION_JSON_VALUE
		)
	)
	public Mono<ResponseEntity<ErrorResponse>> handleBindException(WebExchangeBindException ex) {
		String errors = ex.getBindingResult()
			.getAllErrors()
			.stream()
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.distinct()
			.collect(Collectors.joining("; "));
		ErrorResponse response = ErrorResponse.badRequest(errors);
		return Mono.just(ResponseEntity.status(response.getCode()).contentType(MediaType.APPLICATION_JSON).body(response));
	}
	
	@ExceptionHandler(NotFoundException.class)
	@ApiResponse(
		responseCode = "404",
		description = DESCRIPTION_NOT_FOUND,
		content = @Content(
			schema = @Schema(
				implementation = ErrorResponse.class
			),
			mediaType = MediaType.APPLICATION_JSON_VALUE
		)
	)
	public Mono<ResponseEntity<ErrorResponse>> handleBindException(NotFoundException ex) {
		ErrorResponse response = ErrorResponse.notFound(ex.getMessage());
		return Mono.just(ResponseEntity.status(response.getCode()).contentType(MediaType.APPLICATION_JSON).body(response));
	}
	
}

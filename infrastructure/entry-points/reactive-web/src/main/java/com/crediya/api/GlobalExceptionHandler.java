package com.crediya.api;

import com.crediya.api.contants.ErrorMessage;
import com.crediya.api.contants.swagger.DocApi;
import com.crediya.api.dto.output.ErrorResponseDTO;
import com.crediya.model.exceptions.BusinessException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	@ApiResponse(
	    responseCode = "500",
	    description = DocApi.DESCRIPTION_INTERNAL_ERROR,
	    content = @Content(
			schema = @Schema(
				implementation = ErrorResponseDTO.class
			),
	        mediaType = MediaType.APPLICATION_JSON_VALUE
	    )
	)
	public Mono<ResponseEntity<ErrorResponseDTO>> handleGenericException(Exception ex) {
		log.error(
			"Unhandled exception occurred: {}",
			ex.getMessage(),
			ex
		);
		return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ErrorResponseDTO.internalError(ErrorMessage.GENERIC_ERROR)));
	}
	
	@ExceptionHandler(BusinessException.class)
	@ApiResponse(
		responseCode = "400",
		description = DocApi.DESCRIPTION_BAD_REQUEST,
		content = @Content(
			schema = @Schema(
				implementation = ErrorResponseDTO.class
			),
			mediaType = MediaType.APPLICATION_JSON_VALUE
		)
	)
	public Mono<ResponseEntity<ErrorResponseDTO>> handleBusinessException(BusinessException ex) {
		ErrorResponseDTO response = ErrorResponseDTO.badRequest(ex.getMessage());
		return Mono.just(ResponseEntity.status(response.getCodigo()).contentType(MediaType.APPLICATION_JSON).body(response));
	}
	
}

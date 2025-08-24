package com.crediya.api.dto.output;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {
	
	private String mensaje;
	private int codigo;
	
	private ErrorResponse(String mensaje, int codigo) {
		this.mensaje = mensaje;
		this.codigo = codigo;
	}
	
	public static ErrorResponse badRequest(String mensaje) {
		return new ErrorResponse(mensaje, HttpStatus.BAD_REQUEST.value());
	}
	
	public static ErrorResponse internalError(String mensaje) {
		return new ErrorResponse(mensaje, HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}

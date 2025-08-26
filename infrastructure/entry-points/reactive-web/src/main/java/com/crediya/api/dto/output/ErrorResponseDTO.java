package com.crediya.api.dto.output;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class ErrorResponseDTO {
	
	private String mensaje;
	private int codigo;
	
	private ErrorResponseDTO(String mensaje, int codigo) {
		this.mensaje = mensaje;
		this.codigo = codigo;
	}
	
	public static ErrorResponseDTO badRequest(String mensaje) {
		return new ErrorResponseDTO(mensaje, HttpStatus.BAD_REQUEST.value());
	}
	
	public static ErrorResponseDTO internalError(String mensaje) {
		return new ErrorResponseDTO(mensaje, HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}

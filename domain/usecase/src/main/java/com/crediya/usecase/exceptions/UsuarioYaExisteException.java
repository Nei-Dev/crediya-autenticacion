package com.crediya.usecase.exceptions;

public class UsuarioYaExisteException extends RuntimeException {
	
	public UsuarioYaExisteException(String message) {
		super(message);
	}
	
}

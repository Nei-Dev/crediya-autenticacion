package com.crediya.model.exceptions.usuario;

import com.crediya.model.exceptions.NegocioException;

public class UsuarioYaExisteException extends NegocioException {
	
	public UsuarioYaExisteException(String message) {
		super(message);
	}
	
}

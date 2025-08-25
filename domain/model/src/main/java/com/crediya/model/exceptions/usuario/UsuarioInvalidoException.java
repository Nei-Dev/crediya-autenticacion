package com.crediya.model.exceptions.usuario;

import com.crediya.model.exceptions.NegocioException;

public class UsuarioInvalidoException extends NegocioException {
	public UsuarioInvalidoException(String message) {
		super(message);
	}
}

package com.crediya.model.exceptions.usuario;

import com.crediya.model.exceptions.NegocioException;

public class RolInvalidoException extends NegocioException {
	public RolInvalidoException(String message) {
		super(message);
	}
}

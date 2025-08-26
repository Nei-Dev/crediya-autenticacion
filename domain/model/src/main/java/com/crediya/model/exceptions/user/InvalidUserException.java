package com.crediya.model.exceptions.user;

import com.crediya.model.exceptions.BusinessException;

public class InvalidUserException extends BusinessException {
	public InvalidUserException(String message) {
		super(message);
	}
}

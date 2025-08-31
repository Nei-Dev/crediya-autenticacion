package com.crediya.model.exceptions.user;

import com.crediya.model.exceptions.BusinessException;

public class InvalidLoginException extends BusinessException {
	public InvalidLoginException(String message) {
		super(message);
	}
}

package com.crediya.model.exceptions.user;

import com.crediya.model.exceptions.BusinessException;

public class AlreadyExistsUserException extends BusinessException {
	
	public AlreadyExistsUserException(String message) {
		super(message);
	}
	
}

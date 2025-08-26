package com.crediya.model.exceptions.user;

import com.crediya.model.exceptions.BusinessException;

public class InvalidRoleException extends BusinessException {
	public InvalidRoleException(String message) {
		super(message);
	}
}

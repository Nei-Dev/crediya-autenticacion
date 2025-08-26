package com.crediya.model.exceptions.user;

import com.crediya.model.exceptions.BusinessException;

public class RoleNotFoundException extends BusinessException {
	public RoleNotFoundException(String message) {
		super(message);
	}
}

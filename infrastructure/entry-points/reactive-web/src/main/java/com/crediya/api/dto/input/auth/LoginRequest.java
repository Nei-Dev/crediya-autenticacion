package com.crediya.api.dto.input.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.crediya.api.constants.ValidationMessage.*;

public record LoginRequest(
	@NotNull(message = EMAIL_NOT_BLANK)
	@NotBlank(message = EMAIL_NOT_BLANK)
	@Email(message = EMAIL_FORMAT)
	String email,
	
	@NotNull(message = PASSWORD_NOT_BLANK)
	@NotBlank(message = PASSWORD_NOT_BLANK)
	@Size(min = 5, message = PASSWORD_MIN_LENGTH_5)
	String password
) {}

package com.crediya.model.user;

public record UserClaims (
	Long id,
	String email,
	UserRole role
) {
}

package com.crediya.model.user;

public record UserClaims (
	Long id,
	String email,
	String identification,
	UserRole role
) {
}

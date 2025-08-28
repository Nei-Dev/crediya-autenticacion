package com.crediya.api.dto.output.user;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserResponseDTO(
	Long id,
	String fullname,
	String email,
	String address,
	String phone,
	LocalDate birthDate,
	BigDecimal salaryBase,
	String identification
) {}

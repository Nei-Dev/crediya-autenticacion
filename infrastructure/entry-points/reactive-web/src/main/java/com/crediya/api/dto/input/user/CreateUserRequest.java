package com.crediya.api.dto.input.user;

import java.time.LocalDate;

public record CreateUserRequest(
	
	String nombre,
	String apellido,
	LocalDate fechaNacimiento,
	String direccion,
	String telefono,
	String correoElectronico,
	Integer salarioBase
	
) {}

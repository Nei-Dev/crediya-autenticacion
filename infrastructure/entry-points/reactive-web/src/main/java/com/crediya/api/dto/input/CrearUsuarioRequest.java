package com.crediya.api.dto.input;

import java.time.LocalDate;

public record CrearUsuarioRequest(
	
	String nombre,
	String apellido,
	LocalDate fechaNacimiento,
	String direccion,
	String telefono,
	String correoElectronico,
	Integer salarioBase
	
) {}

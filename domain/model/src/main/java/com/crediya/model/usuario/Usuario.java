package com.crediya.model.usuario;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
	
	private Long idUsuario;
	private String nombre;
	private String apellidos;
	private LocalDate fechaNacimiento;
	private String direccion;
	private String telefono;
	private String correoElectronico;
	private Integer salarioBase;
	private RolUsuario rol;

}

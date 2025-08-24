package com.crediya.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("usuario")
public class UsuarioData {
	
	@Id
	@Column("id")
	private Long id;

	@Column("id_rol")
	private Long idRol;

	@Column("nombre")
	private String nombre;

	@Column("apellido")
	private String apellido;
//
//	@Column("documento_identidad")
//	private String documentoIdentidad;

	@Column("correo_electronico")
	private String correoElectronico;

	@Column("salario_base")
	private Integer salarioBase;

	@Column("fecha_nacimiento")
	private LocalDate fechaNacimiento;

	@Column("direccion")
	private String direccion;

	@Column("telefono")
	private String telefono;
}

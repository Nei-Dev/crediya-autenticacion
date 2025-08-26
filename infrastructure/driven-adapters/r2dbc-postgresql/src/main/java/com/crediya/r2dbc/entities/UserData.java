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
public class UserData {
	
	@Id
	@Column("id")
	private Long id;

	@Column("id_rol")
	private Long idRole;

	@Column("nombre")
	private String name;

	@Column("apellido")
	private String lastname;

	@Column("correo_electronico")
	private String email;

	@Column("salario_base")
	private Integer salaryBase;

	@Column("fecha_nacimiento")
	private LocalDate birthDate;

	@Column("direccion")
	private String address;

	@Column("telefono")
	private String phone;
}

package com.crediya.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rol")
public class RoleData {
	
	@Id
	@Column("id")
	private Long id;
	
	@Column("nombre")
	private String name;
	
	@Column("descripcion")
	private String description;
	
}

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
@Table(name = "crediya_role")
public class RoleData {
	
	@Id
	@Column("id")
	private Long id;
	
	@Column("name")
	private String name;
	
	@Column("description")
	private String description;
	
}

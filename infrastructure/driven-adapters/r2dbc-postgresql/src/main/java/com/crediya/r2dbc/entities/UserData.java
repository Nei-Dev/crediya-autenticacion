package com.crediya.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("crediya_user")
public class UserData {
	
	@Id
	@Column("id")
	private Long id;

	@Column("id_role")
	private Long idRole;

	@Column("name")
	private String name;

	@Column("lastname")
	private String lastname;

	@Column("email")
	private String email;

	@Column("salary_base")
	private BigDecimal salaryBase;

	@Column("birth_date")
	private LocalDate birthDate;

	@Column("address")
	private String address;

	@Column("phone")
	private String phone;
	
	@Column("identification")
	private String identification;
	
	@Column("password")
	private String password;
}

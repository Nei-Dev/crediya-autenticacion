package com.crediya.model.user;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
	
	private Long idUser;
	private String name;
	private String lastname;
	private LocalDate birthDate;
	private String address;
	private String phone;
	private String email;
	private BigDecimal salaryBase;
	private String identification;
	private UserRole role;

}

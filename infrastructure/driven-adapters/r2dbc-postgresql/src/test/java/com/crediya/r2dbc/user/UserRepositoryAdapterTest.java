package com.crediya.r2dbc.user;

import com.crediya.model.exceptions.user.RoleNotFoundException;
import com.crediya.model.user.User;
import com.crediya.model.user.UserRole;
import com.crediya.r2dbc.constants.ErrorMessage;
import com.crediya.r2dbc.entities.RoleData;
import com.crediya.r2dbc.entities.UserData;
import com.crediya.r2dbc.role.RolReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {
	@InjectMocks
	private UserRepositoryAdapter adapter;
	
	@Mock
	private UserReactiveRepository userReactiveRepository;
	@Mock
	private RolReactiveRepository rolReactiveRepository;
	@Mock
	private TransactionalOperator transactionalOperator;
	
	private User user;
	private UserData userData;
	private RoleData roleData;
	
	@BeforeEach
	void setUp() {
		String nombre = "Juan";
		String apellidos = "Perez";
		LocalDate fechaNacimiento = LocalDate.of(1990, 1, 1);
		String direccion = "Calle Falsa 123";
		String telefono = "555-1234";
		String correoElectronico = "juan@perez.com";
		BigDecimal salarioBase = BigDecimal.valueOf(5_000_000);
		
		roleData = RoleData.builder()
			.id(3L)
			.name(UserRole.CLIENT.name())
			.description("Cliente con acceso a solicitudes de servicios")
			.build();
		user = User.builder()
			.name(nombre)
			.lastname(apellidos)
			.birthDate(fechaNacimiento)
			.address(direccion)
			.phone(telefono)
			.email(correoElectronico)
			.salaryBase(salarioBase)
			.role(UserRole.CLIENT)
			.build();
		userData = UserData.builder()
			.id(1L)
			.idRole(roleData.getId())
			.name(nombre)
			.lastname(apellidos)
			.birthDate(fechaNacimiento)
			.address(direccion)
			.phone(telefono)
			.email(correoElectronico)
			.salaryBase(salarioBase)
			.build();
		
	}
	
	@Test
	@SuppressWarnings("unchecked")
	void shouldBeCreateUserSuccesfully() {
		when(rolReactiveRepository.findByName(UserRole.CLIENT.name())).thenReturn(Mono.just(roleData));
		when(rolReactiveRepository.findById(roleData.getId())).thenReturn(Mono.just(roleData));
		when(userReactiveRepository.save(any(UserData.class))).thenReturn(Mono.just(userData));
		when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		StepVerifier.create(adapter.createUser(user))
			.expectNextMatches(u -> u.getName()
				.equals("Juan") && u.getLastname()
				.equals("Perez") && u.getBirthDate()
				.equals(LocalDate.of(1990, 1, 1)) && u.getAddress()
				.equals("Calle Falsa 123") && u.getPhone()
				.equals("555-1234") && u.getEmail()
				.equals("juan@perez.com") && u.getSalaryBase()
				.equals(BigDecimal.valueOf(5_000_000)) && u.getRole() == UserRole.CLIENT)
			.verifyComplete();
	}
	
	@Test
	void shouldThrowExceptionIfRoleNotExists() {
		when(rolReactiveRepository.findByName("CLIENT")).thenReturn(Mono.empty());
		when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		StepVerifier.create(adapter.createUser(user))
			.expectErrorMatches(e -> e instanceof RoleNotFoundException && e.getMessage()
				.equals(ErrorMessage.ROLE_NOT_FOUND))
			.verify();
	}
	
}


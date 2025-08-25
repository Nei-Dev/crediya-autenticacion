package com.crediya.r2dbc.usuario;

import com.crediya.model.exceptions.usuario.RolInvalidoException;
import com.crediya.model.usuario.RolUsuario;
import com.crediya.model.usuario.Usuario;
import com.crediya.r2dbc.entities.RolData;
import com.crediya.r2dbc.entities.UsuarioData;
import com.crediya.r2dbc.rol.RolReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryAdapterTest {
	@InjectMocks
	private UsuarioRepositoryAdapter adapter;
	
	@Mock
	private UsuarioReactiveRepository usuarioReactiveRepository;
	@Mock
	private RolReactiveRepository rolReactiveRepository;
	@Mock
	private TransactionalOperator transactionalOperator;
	
	private Usuario usuario;
	private UsuarioData usuarioData;
	private RolData rolData;
	
	@BeforeEach
	void setUp() {
		String nombre = "Juan";
		String apellidos = "Perez";
		LocalDate fechaNacimiento = LocalDate.of(1990, 1, 1);
		String direccion = "Calle Falsa 123";
		String telefono = "555-1234";
		String correoElectronico = "juan@perez.com";
		Integer salarioBase = 5_000_000;
		
		rolData = RolData.builder()
			.id(3L)
			.nombre(RolUsuario.CLIENTE.name())
			.descripcion("Cliente con acceso a solicitudes de servicios")
			.build();
		usuario = Usuario.builder()
			.nombre(nombre)
			.apellidos(apellidos)
			.fechaNacimiento(fechaNacimiento)
			.direccion(direccion)
			.telefono(telefono)
			.correoElectronico(correoElectronico)
			.salarioBase(salarioBase)
			.rol(RolUsuario.CLIENTE)
			.build();
		usuarioData = UsuarioData.builder()
			.id(1L)
			.idRol(rolData.getId())
			.nombre(nombre)
			.apellido(apellidos)
			.fechaNacimiento(fechaNacimiento)
			.direccion(direccion)
			.telefono(telefono)
			.correoElectronico(correoElectronico)
			.salarioBase(salarioBase)
			.build();
		
	}
	
	@Test
	@SuppressWarnings("unchecked")
	void debeRegistrarUsuarioCorrectamente() {
		when(rolReactiveRepository.findByNombre(RolUsuario.CLIENTE.name())).thenReturn(Mono.just(rolData));
		when(rolReactiveRepository.findById(rolData.getId())).thenReturn(Mono.just(rolData));
		when(usuarioReactiveRepository.save(any(UsuarioData.class))).thenReturn(Mono.just(usuarioData));
		when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		StepVerifier.create(adapter.registrarUsuario(usuario))
			.expectNextMatches(u -> u.getNombre().equals("Juan")
				&& u.getApellidos().equals("Perez")
				&& u.getFechaNacimiento().equals(LocalDate.of(1990, 1, 1))
				&& u.getDireccion().equals("Calle Falsa 123")
				&& u.getTelefono().equals("555-1234")
				&& u.getCorreoElectronico().equals("juan@perez.com")
				&& u.getSalarioBase().equals(5_000_000)
				&& u.getRol() == RolUsuario.CLIENTE)
			.verifyComplete();
	}
	
	@Test
	void debeLanzarExcepcionCuandoElRolNoExiste() {
		when(rolReactiveRepository.findByNombre("CLIENTE")).thenReturn(Mono.empty());
		when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		StepVerifier.create(adapter.registrarUsuario(usuario))
			.expectErrorMatches(e -> e instanceof RolInvalidoException && e.getMessage()
				.contains("no existe en la base de datos"))
			.verify();
	}
	
}


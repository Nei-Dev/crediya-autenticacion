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
		rolData = RolData.builder()
			.id(3L)
			.nombre(RolUsuario.CLIENTE.name())
			.descripcion("Cliente con acceso a solicitudes de servicios")
			.build();
		usuario = Usuario.builder()
			.nombre("Juan")
			.apellidos("Perez")
			.correoElectronico("juan@correo.com")
			.rol(RolUsuario.CLIENTE)
			.build();
		usuarioData = UsuarioData.builder()
			.id(1L)
			.idRol(rolData.getId())
			.nombre("Juan")
			.apellido("Perez")
			.correoElectronico("juan@correo.com")
			.build();
			
	}
	
	@Test
	void debeRegistrarUsuarioCorrectamente() {
		when(rolReactiveRepository.findByNombre(RolUsuario.CLIENTE.name())).thenReturn(Mono.just(rolData));
		when(rolReactiveRepository.findById(rolData.getId())).thenReturn(Mono.just(rolData));
		when(usuarioReactiveRepository.save(any(UsuarioData.class))).thenReturn(Mono.just(usuarioData));
		when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		StepVerifier.create(adapter.registrarUsuario(usuario))
			.expectNextMatches(u -> u.getCorreoElectronico()
				.equals("juan@correo.com"))
			.verifyComplete();
	}
	
	@Test
	void debeLanzarExcepcionCuandoElRolNoExiste() {
		when(rolReactiveRepository.findByNombre("UNKNOWN")).thenReturn(Mono.empty());
		when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		StepVerifier.create(adapter.registrarUsuario(usuario))
			.expectErrorMatches(e -> e instanceof RolInvalidoException && e.getMessage()
				.contains("no es válido"))
			.verify();
	}
	
	@Test
	void debeLanzarExcepcionCuandoElCorreoElectronicoYaEstaRegistrado() {
		when(usuarioReactiveRepository.findByCorreoElectronico("juan@correo.com")).thenReturn(Mono.just(usuarioData));
		StepVerifier.create(adapter.buscarPorCorreoElectronico("juan@correo.com"))
			.expectNextMatches(u -> u.getCorreoElectronico()
				.equals("juan@correo.com"))
			.verifyComplete();
	}
	
}


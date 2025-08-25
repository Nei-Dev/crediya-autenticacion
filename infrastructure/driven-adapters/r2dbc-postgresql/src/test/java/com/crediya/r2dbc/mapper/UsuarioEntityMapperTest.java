package com.crediya.r2dbc.mapper;

import com.crediya.model.usuario.Usuario;
import com.crediya.r2dbc.entities.UsuarioData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioEntityMapperTest {
	
	private Usuario usuario;
	private UsuarioData usuarioData;
	
	@BeforeEach
	void setUp() {
		String nombre = "Juan";
		String apellidos = "Perez";
		LocalDate fechaNacimiento = LocalDate.of(1990, 1, 1);
		String direccion = "Calle Falsa 123";
		String telefono = "555-1234";
		String correoElectronico = "juan@perez.com";
		Integer salarioBase = 5_000_000;
		
		usuario = Usuario.builder()
			.idUsuario(1L)
			.nombre(nombre)
			.apellidos(apellidos)
			.fechaNacimiento(fechaNacimiento)
			.direccion(direccion)
			.telefono(telefono)
			.correoElectronico(correoElectronico)
			.salarioBase(salarioBase)
			.build();
		usuarioData = UsuarioData.builder()
			.id(1L)
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
	void debeConvertirDeDataAEntity() {
		Usuario resultado = UsuarioEntityMapper.toEntity(usuarioData);
		
		assertNotNull(resultado);
		assertEquals(usuarioData.getId(), resultado.getIdUsuario());
		assertEquals(usuarioData.getNombre(), resultado.getNombre());
		assertEquals(usuarioData.getApellido(), resultado.getApellidos());
		assertEquals(usuarioData.getFechaNacimiento(), resultado.getFechaNacimiento());
		assertEquals(usuarioData.getDireccion(), resultado.getDireccion());
		assertEquals(usuarioData.getTelefono(), resultado.getTelefono());
		assertEquals(usuarioData.getCorreoElectronico(), resultado.getCorreoElectronico());
		assertEquals(usuarioData.getSalarioBase(), resultado.getSalarioBase());
	}
	
	@Test
	void debeConvertirDeEntityAData() {
		UsuarioData resultado = UsuarioEntityMapper.toData(usuario);
		
		assertNotNull(resultado);
		assertEquals(usuario.getIdUsuario(), resultado.getId());
		assertEquals(usuario.getNombre(), resultado.getNombre());
		assertEquals(usuario.getApellidos(), resultado.getApellido());
		assertEquals(usuario.getFechaNacimiento(), resultado.getFechaNacimiento());
		assertEquals(usuario.getDireccion(), resultado.getDireccion());
		assertEquals(usuario.getTelefono(), resultado.getTelefono());
		assertEquals(usuario.getCorreoElectronico(), resultado.getCorreoElectronico());
		assertEquals(usuario.getSalarioBase(), resultado.getSalarioBase());
	}
	
	@Test
	void debeConvertirCamposVaciosYNulos() {
		usuarioData.setId(null);
		usuarioData.setNombre(null);
		usuarioData.setApellido("");
		usuarioData.setFechaNacimiento(null);
		usuarioData.setDireccion("");
		usuarioData.setTelefono(null);
		usuarioData.setCorreoElectronico("");
		usuarioData.setSalarioBase(null);
		
		Usuario resultado = UsuarioEntityMapper.toEntity(usuarioData);
		
		assertNotNull(resultado);
		assertNull(resultado.getIdUsuario());
		assertNull(resultado.getNombre());
		assertEquals("", resultado.getApellidos());
		assertNull(resultado.getFechaNacimiento());
		assertEquals("", resultado.getDireccion());
		assertNull(resultado.getTelefono());
		assertEquals("", resultado.getCorreoElectronico());
		assertNull(resultado.getSalarioBase());
	}
	
}

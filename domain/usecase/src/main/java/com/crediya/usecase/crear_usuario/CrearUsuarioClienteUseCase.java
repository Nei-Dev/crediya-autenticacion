package com.crediya.usecase.crear_usuario;

import com.crediya.model.constants.LimitesSalarioBase;
import com.crediya.model.constants.MensajeError;
import com.crediya.model.constants.Regex;
import com.crediya.model.usuario.RolUsuario;
import com.crediya.model.usuario.Usuario;
import com.crediya.model.usuario.gateways.UsuarioRepository;
import com.crediya.model.usuario.ports.ICrearUsuarioClienteUseCase;
import com.crediya.model.exceptions.usuario.UsuarioInvalidoException;
import com.crediya.model.exceptions.usuario.UsuarioYaExisteException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@RequiredArgsConstructor
public class CrearUsuarioClienteUseCase implements ICrearUsuarioClienteUseCase {
	
	private final UsuarioRepository usuarioRepository;
	
	private final Logger logger = Logger.getLogger(CrearUsuarioClienteUseCase.class.getName());
	
	@Override
	public Mono<Usuario> execute(Usuario usuario) {
		
		try {
			validarUsuario(usuario);
		} catch (UsuarioInvalidoException e) {
			return Mono.error(e);
		}
		
		usuario.setRol(RolUsuario.CLIENTE);
		
		return usuarioRepository.buscarPorCorreoElectronico(usuario.getCorreoElectronico())
			.doOnSubscribe(sub -> logger.info("CrearUsuarioClienteUseCase - Verificando si el usuario con correo " + usuario.getCorreoElectronico() + " existe"))
			.hasElement()
			.doOnNext(existe -> logger.info(
				"CrearUsuarioClienteUseCase - Usuario con correo " + usuario.getCorreoElectronico() + ( Boolean.TRUE.equals(existe) ? " existe" : " no existe" )))
			.doOnError(error -> logger.severe("CrearUsuarioClienteUseCase - Error al verificar si el usuario con correo " + usuario.getCorreoElectronico()))
			.flatMap(usuarioExiste -> {
				if (Boolean.TRUE.equals(usuarioExiste)) {
					return Mono.error(new UsuarioYaExisteException("El usuario con correo " + usuario.getCorreoElectronico() + " ya existe"));
				}
				return usuarioRepository.registrarUsuario(usuario)
					.doOnSubscribe(sub -> logger.info("CrearUsuarioClienteUseCase - Registrando usuario con correo " + usuario.getCorreoElectronico()))
					.doOnSuccess(usuarioGuardado -> logger.info("CrearUsuarioClienteUseCase - Usuario registrado con ID " + usuarioGuardado.getIdUsuario()))
					.doOnError(error -> logger.severe(
						"CrearUsuarioClienteUseCase - Error al registrar el usuario con correo " + usuario.getCorreoElectronico() + ": " + error.getMessage()));
			});
	}
	
	private void validarUsuario(Usuario usuario) {
		if (usuario == null) throw new UsuarioInvalidoException("El usuario no puede ser nulo");
		
		validarNombres(usuario.getNombre());
		validarApellidos(usuario.getApellidos());
		validarCorreoElectronico(usuario.getCorreoElectronico());
		validarSalarioBase(usuario.getSalarioBase());
	}
	
	private void validarNombres(String nombres) {
		if (nombres == null || nombres.trim()
			.isEmpty()) {
			throw new UsuarioInvalidoException(MensajeError.NOMBRE_USUARIO_VACIO);
		}
	}
	
	private void validarApellidos(String apellidos) {
		if (apellidos == null || apellidos.trim()
			.isEmpty()) {
			throw new UsuarioInvalidoException(MensajeError.APELLIDOS_USUARIO_VACIO);
		}
	}
	
	private void validarCorreoElectronico(String correoElectronico) {
		if (correoElectronico == null || correoElectronico.trim()
			.isEmpty()) {
			throw new UsuarioInvalidoException(MensajeError.CORREO_ELECTRONICO_VACIO);
		}
		if (!correoElectronico.matches(Regex.EMAIL)) {
			throw new UsuarioInvalidoException(MensajeError.CORREO_ELECTRONICO_INVALIDO);
		}
	}
	
	private void validarSalarioBase(Integer salarioBase) {
		if (salarioBase == null || salarioBase < LimitesSalarioBase.MINIMO || salarioBase > LimitesSalarioBase.MAXIMO) {
			throw new UsuarioInvalidoException(MensajeError.SALARIO_BASE_INVALIDO);
		}
	}
}

package com.crediya.usecase.crear_usuario;

import com.crediya.model.constants.LimitesSalarioBase;
import com.crediya.model.constants.MensajeError;
import com.crediya.model.constants.Regex;
import com.crediya.model.exceptions.UsuarioInvalidoException;
import com.crediya.model.usuario.RolUsuario;
import com.crediya.model.usuario.Usuario;
import com.crediya.model.usuario.gateways.UsuarioRepository;
import com.crediya.model.usuario.ports.ICrearUsuarioUseCase;
import com.crediya.usecase.exceptions.UsuarioYaExisteException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@RequiredArgsConstructor
public class CrearUsuarioUseCase implements ICrearUsuarioUseCase {
	
	private final UsuarioRepository usuarioRepository;
	
	private final Logger logger = Logger.getLogger(CrearUsuarioUseCase.class.getName());
	
	@Override
	public Mono<Usuario> execute(Usuario usuario) {
		
		if (usuario == null) {
			return Mono.error(new UsuarioInvalidoException("El usuario no puede ser nulo"));
		}
		
		validarUsuario(usuario);
		usuario.setRol(RolUsuario.CLIENTE);
		
		return usuarioRepository.buscarPorCorreoElectronico(usuario.getCorreoElectronico())
			.doOnSubscribe(sub -> logger.info("CrearUsuarioUseCase - Verificando si el usuario con correo " + usuario.getCorreoElectronico() + " existe"))
			.hasElement()
			.doOnNext(existe -> logger.info("CrearUsuarioUseCase - Usuario con correo " + usuario.getCorreoElectronico() + (Boolean.TRUE.equals(existe) ? " existe" : " no existe")))
			.doOnError(error -> logger.severe("CrearUsuarioUseCase - Error al verificar si el usuario con correo " + usuario.getCorreoElectronico()))
			.flatMap(usuarioExiste -> {
				if (Boolean.TRUE.equals(usuarioExiste)) {
					return Mono.error(new UsuarioYaExisteException("El usuario con correo " + usuario.getCorreoElectronico() + " ya existe"));
				}
				return usuarioRepository.registrarUsuario(usuario)
					.doOnSubscribe(sub -> logger.info("CrearUsuarioUseCase - Registrando usuario con correo " + usuario.getCorreoElectronico()))
					.doOnSuccess(usuarioGuardado -> logger.info("CrearUsuarioUseCase - Usuario registrado con ID " + usuarioGuardado.getIdUsuario()))
					.doOnError(error -> logger.severe("CrearUsuarioUseCase - Error al registrar el usuario con correo " + usuario.getCorreoElectronico() + ": " + error.getMessage()));
			});
	}
	
	private void validarUsuario(Usuario usuario) {
		validarNombres(usuario.getNombre());
		validarApellidos(usuario.getApellidos());
		validarCorreoElectronico(usuario.getCorreoElectronico());
		validarSalarioBase(usuario.getSalarioBase());
	}

	private void validarNombres(String nombres) {
		if (nombres == null || nombres.trim().isEmpty()) {
			throw new UsuarioInvalidoException(MensajeError.NOMBRE_USUARIO_VACIO);
		}
	}

	private void validarApellidos(String apellidos) {
		if (apellidos == null || apellidos.trim().isEmpty()) {
			throw new UsuarioInvalidoException(MensajeError.APELLIDOS_USUARIO_VACIO);
		}
	}

	private void validarCorreoElectronico(String correoElectronico) {
		if (correoElectronico == null || correoElectronico.trim().isEmpty()) {
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

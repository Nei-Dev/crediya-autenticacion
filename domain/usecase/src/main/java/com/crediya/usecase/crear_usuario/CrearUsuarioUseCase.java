package com.crediya.usecase.crear_usuario;

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
		usuario.validar();
		return usuarioRepository.existsByCorreoElectronico(usuario.getCorreoElectronico())
			.doOnSubscribe(subscription -> logger.info("Verificando si el usuario con correo " + usuario.getCorreoElectronico() + " ya existe"))
			.flatMap(exists -> {
				if (Boolean.TRUE.equals(exists)) {
					return Mono.error(new UsuarioYaExisteException("El usuario con correo " + usuario.getCorreoElectronico() + " ya existe"));
				}
				return usuarioRepository.registrarUsuarioSolicitante(usuario);
			});
	}
}

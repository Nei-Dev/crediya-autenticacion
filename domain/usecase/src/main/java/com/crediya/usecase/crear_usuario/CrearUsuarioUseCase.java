package com.crediya.usecase.crear_usuario;

import com.crediya.model.usuario.Usuario;
import com.crediya.model.usuario.gateways.UsuarioRepository;
import com.crediya.usecase.exceptions.UsuarioYaExisteException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CrearUsuarioUseCase implements ICrearUsuarioUseCase {
	
	private final UsuarioRepository usuarioRepository;
	
	@Override
	public Mono<Usuario> execute(Usuario usuario) {
		usuario.validar();
		return usuarioRepository.existsByCorreoElectronico(usuario.getCorreoElectronico())
			.log("CrearUsuarioUseCase - Verificando existencia de usuario")
			.flatMap(exists -> {
				if (Boolean.TRUE.equals(exists)) {
					return Mono.error(new UsuarioYaExisteException("El usuario con correo " + usuario.getCorreoElectronico() + " ya existe"));
				}
				return usuarioRepository.registrarUsuarioSolicitante(usuario);
			});
	}
}

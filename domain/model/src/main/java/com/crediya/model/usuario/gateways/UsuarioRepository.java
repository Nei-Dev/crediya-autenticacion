package com.crediya.model.usuario.gateways;

import com.crediya.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {
	
	Mono<Usuario> registrarUsuarioSolicitante(Usuario usuario);
	
	Mono<Boolean> existsByCorreoElectronico(String correoElectronico);
	
}

package com.crediya.model.usuario.ports;

import com.crediya.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface ICrearUsuarioUseCase {
	
	Mono<Usuario> execute(Usuario usuario);
	
}

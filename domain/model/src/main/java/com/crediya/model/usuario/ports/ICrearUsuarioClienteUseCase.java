package com.crediya.model.usuario.ports;

import com.crediya.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface ICrearUsuarioClienteUseCase {
	
	Mono<Usuario> execute(Usuario usuario);
	
}

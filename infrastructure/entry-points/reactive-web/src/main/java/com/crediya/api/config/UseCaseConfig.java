package com.crediya.api.config;

import com.crediya.model.usuario.gateways.UsuarioRepository;
import com.crediya.usecase.crear_usuario.CrearUsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
	
	@Bean
	public CrearUsuarioUseCase crearUsuarioUseCase(
		UsuarioRepository usuarioRepository
	) {
		return new CrearUsuarioUseCase(usuarioRepository);
	}
}

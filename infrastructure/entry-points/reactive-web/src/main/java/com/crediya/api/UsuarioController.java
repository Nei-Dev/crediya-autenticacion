package com.crediya.api;

import com.crediya.api.dto.input.CrearUsuarioRequest;
import com.crediya.model.usuario.Usuario;
import com.crediya.usecase.crear_usuario.CrearUsuarioClienteClienteUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UsuarioController {
	
	private final CrearUsuarioClienteClienteUseCase crearUsuarioClienteUseCase;
	
	@PostMapping
	public Mono<Usuario> crearUsuarioCliente(@RequestBody CrearUsuarioRequest crearUsuarioRequest) {
		return crearUsuarioClienteUseCase.execute(Usuario.builder()
			.nombre(crearUsuarioRequest.nombre())
			.apellidos(crearUsuarioRequest.apellido())
			.fechaNacimiento(crearUsuarioRequest.fechaNacimiento())
			.direccion(crearUsuarioRequest.direccion())
			.telefono(crearUsuarioRequest.telefono())
			.correoElectronico(crearUsuarioRequest.correoElectronico())
			.salarioBase(crearUsuarioRequest.salarioBase())
			.build());
	}
}

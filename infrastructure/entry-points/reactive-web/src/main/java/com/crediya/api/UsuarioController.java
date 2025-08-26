package com.crediya.api;

import com.crediya.api.dto.input.CrearUsuarioRequest;
import com.crediya.model.usuario.Usuario;
import com.crediya.usecase.crear_usuario.CrearUsuarioClienteUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping(value = "/api/v1/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UsuarioController {
	
	private final CrearUsuarioClienteUseCase crearUsuarioClienteUseCase;
	
	@Operation(
        summary = "Crear usuario cliente",
        description = "Crea un nuevo usuario cliente en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
    })
    @PostMapping
    public Mono<ResponseEntity<Usuario>> crearUsuarioCliente(@RequestBody CrearUsuarioRequest crearUsuarioRequest) {
        return crearUsuarioClienteUseCase.execute(Usuario.builder()
            .nombre(crearUsuarioRequest.nombre())
            .apellidos(crearUsuarioRequest.apellido())
            .fechaNacimiento(crearUsuarioRequest.fechaNacimiento())
            .direccion(crearUsuarioRequest.direccion())
            .telefono(crearUsuarioRequest.telefono())
            .correoElectronico(crearUsuarioRequest.correoElectronico())
            .salarioBase(crearUsuarioRequest.salarioBase())
            .build())
	        .map(usuarioCreado -> ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado));
    }
}

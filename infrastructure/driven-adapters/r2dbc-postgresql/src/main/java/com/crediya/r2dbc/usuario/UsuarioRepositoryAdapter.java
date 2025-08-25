package com.crediya.r2dbc.usuario;

import com.crediya.model.exceptions.usuario.RolInvalidoException;
import com.crediya.model.usuario.RolUsuario;
import com.crediya.model.usuario.Usuario;
import com.crediya.model.usuario.gateways.UsuarioRepository;
import com.crediya.r2dbc.entities.UsuarioData;
import com.crediya.r2dbc.mapper.UsuarioEntityMapper;
import com.crediya.r2dbc.rol.RolReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioRepository {
	
	private final UsuarioReactiveRepository repository;
	private final RolReactiveRepository rolReactiveRepository;
	private final TransactionalOperator transactionalOperator;
	
	@Override
	public Mono<Usuario> registrarUsuario(Usuario usuario) {
		UsuarioData usuarioData = UsuarioEntityMapper.toData(usuario);
		String rolUsuario = usuario.getRol()
			.name();
		return transactionalOperator.transactional(rolReactiveRepository.findByNombre(rolUsuario)
			.doOnSubscribe(subscription -> log.trace("RegistrandoUsuario - Buscando rol en la base de datos: {}", rolUsuario))
			.doOnNext(rol -> log.debug("RegistrandoUsuario - Rol encontrado: {}", rol.getNombre()))
			.doOnError(error -> log.error("RegistrandoUsuario - Error al buscar el rol {}: {}", rolUsuario, error.getMessage()))
			.switchIfEmpty(Mono.error(new RolInvalidoException("El rol" + rolUsuario + " no existe en la base de datos")))
			.flatMap(rol -> {
				usuarioData.setIdRol(rol.getId());
				return repository.save(usuarioData)
					.doOnSubscribe(subscription -> log.trace("RegistrandoUsuario - Guardando usuario en la base de datos: {}", usuarioData.getCorreoElectronico()))
					.doOnNext(savedUsuario -> log.debug("RegistrandoUsuario - Usuario guardado con ID: {}", savedUsuario.getId()))
					.doOnError(error -> log.error("RegistrandoUsuario - Error al guardar el usuario {}: {}", usuarioData.getCorreoElectronico(), error.getMessage()));
			})
			.flatMap(this::mapToUsuario));
	}
	
	@Override
	public Mono<Usuario> buscarPorCorreoElectronico(String correoElectronico) {
		return repository.findByCorreoElectronico(correoElectronico)
			.doOnSubscribe(subscription -> log.trace("BuscarPorCorreoElectronico - Buscando usuario en la base de datos: {}", correoElectronico))
			.doOnNext(usuarioData -> log.debug("BuscarPorCorreoElectronico - Usuario encontrado con ID: {}", usuarioData.getId()))
			.doOnError(error -> log.error("BuscarPorCorreoElectronico - Error al buscar el usuario {}: {}", correoElectronico, error.getMessage()))
			.flatMap(this::mapToUsuario);
	}
	
	private Mono<Usuario> mapToUsuario(UsuarioData usuarioData) {
		if (usuarioData == null) {
			return Mono.empty();
		}
		return rolReactiveRepository.findById(usuarioData.getIdRol())
			.switchIfEmpty(Mono.error(new RolInvalidoException("El rol con id " + usuarioData.getIdRol() + " no existe en la base de datos")))
			.doOnError(error -> log.error("Error al buscar el rol con id {}: {}", usuarioData.getIdRol(), error.getMessage()))
			.map(rolData -> {
				Usuario usuario = UsuarioEntityMapper.toEntity(usuarioData);
				usuario.setRol(RolUsuario.valueOf(rolData.getNombre()));
				return usuario;
			});
	}
}

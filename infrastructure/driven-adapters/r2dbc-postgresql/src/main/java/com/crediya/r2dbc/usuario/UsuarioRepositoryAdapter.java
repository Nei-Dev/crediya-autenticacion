package com.crediya.r2dbc.usuario;

import com.crediya.model.exceptions.RolInvalidoException;
import com.crediya.model.usuario.Usuario;
import com.crediya.model.usuario.gateways.UsuarioRepository;
import com.crediya.r2dbc.entities.UsuarioData;
import com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import com.crediya.r2dbc.mapper.UsuarioEntityMapper;
import com.crediya.r2dbc.rol.RolReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class UsuarioRepositoryAdapter extends ReactiveAdapterOperations<Usuario, UsuarioData, Long, UsuarioReactiveRepository> implements UsuarioRepository {
	
	private final RolReactiveRepository rolReactiveRepository;
	private final TransactionalOperator transactionalOperator;
	
	public UsuarioRepositoryAdapter(UsuarioReactiveRepository repository, RolReactiveRepository rolReactiveRepository, TransactionalOperator transactionalOperator) {
		super(repository, UsuarioEntityMapper::toData, UsuarioEntityMapper::toEntity);
		this.rolReactiveRepository = rolReactiveRepository;
		this.transactionalOperator = transactionalOperator;
	}
	
	@Override
	public Mono<Usuario> registrarUsuarioSolicitante(Usuario usuario) {
		UsuarioData usuarioData = UsuarioEntityMapper.toData(usuario);
		String rolUsuario = usuario.getRol().name();
		return transactionalOperator.transactional(rolReactiveRepository.findByNombre(rolUsuario)
			.switchIfEmpty(Mono.error(new RolInvalidoException("El rol" + rolUsuario + " no existe en la base de datos")))
			.flatMap(rol -> {
				usuarioData.setIdRol(rol.getId());
				return super.saveData(usuarioData)
					.map(this::toEntity);
			}));
	}
	
	@Override
	public Mono<Boolean> existsByCorreoElectronico(String correoElectronico) {
		return repository.existsByCorreoElectronico(correoElectronico);
	}
}

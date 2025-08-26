package com.crediya.r2dbc.user;

import com.crediya.model.exceptions.user.RoleNotFoundException;
import com.crediya.model.user.User;
import com.crediya.model.user.UserRole;
import com.crediya.model.user.gateways.UserRepository;
import com.crediya.r2dbc.constants.ErrorMessage;
import com.crediya.r2dbc.entities.UserData;
import com.crediya.r2dbc.mapper.UsuarioEntityMapper;
import com.crediya.r2dbc.role.RolReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
	
	private final UserReactiveRepository repository;
	private final RolReactiveRepository rolReactiveRepository;
	private final TransactionalOperator transactionalOperator;
	
	@Override
	public Mono<User> createUser(User user) {
		UserData userData = UsuarioEntityMapper.INSTANCE.toData(user);
		String userRole = user.getRole()
			.name();
		return transactionalOperator.transactional(rolReactiveRepository.findByName(userRole)
			.doOnSubscribe(subscription -> log.trace("Searching role in the database: {}", userRole))
			.switchIfEmpty(Mono.error(new RoleNotFoundException(ErrorMessage.ROLE_NOT_FOUND)))
			.flatMap(rol -> {
				userData.setIdRole(rol.getId());
				return repository.save(userData)
					.doOnSubscribe(subscription -> log.trace("Creating user in the database: {}", userData.getEmail()));
			})
			.flatMap(this::mapToUser));
	}
	
	@Override
	public Mono<User> getByEmail(String email) {
		return repository.findByEmail(email)
			.doOnSubscribe(subscription -> log.trace("Searching user in the database: {}", email))
			.flatMap(this::mapToUser);
	}
	
	private Mono<User> mapToUser(UserData userData) {
		if (userData == null) {
			return Mono.empty();
		}
		return rolReactiveRepository.findById(userData.getIdRole())
			.switchIfEmpty(Mono.error(new RoleNotFoundException(ErrorMessage.ROLE_NOT_FOUND)))
			.map(rolData -> {
				User user = UsuarioEntityMapper.INSTANCE.toEntity(userData);
				user.setRole(UserRole.valueOf(rolData.getName()));
				return user;
			});
	}
}

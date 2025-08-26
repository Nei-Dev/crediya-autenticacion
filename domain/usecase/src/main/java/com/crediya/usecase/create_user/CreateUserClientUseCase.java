package com.crediya.usecase.create_user;

import com.crediya.model.constants.ErrorMessage;
import com.crediya.model.constants.Regex;
import com.crediya.model.constants.SalaryBaseLimits;
import com.crediya.model.exceptions.user.AlreadyExistsUserException;
import com.crediya.model.exceptions.user.InvalidUserException;
import com.crediya.model.user.User;
import com.crediya.model.user.UserRole;
import com.crediya.model.user.gateways.UserRepository;
import com.crediya.model.user.ports.ICreateUserClientUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class CreateUserClientUseCase implements ICreateUserClientUseCase {
	
	private final UserRepository userRepository;
	
	@Override
	public Mono<User> execute(User user) {
		try {
			validateUser(user);
		} catch (InvalidUserException e) {
			return Mono.error(e);
		}

		user.setRole(UserRole.CLIENT);

		return userRepository.getByEmail(user.getEmail())
			.hasElement()
			.flatMap(userExists -> {
				if (Boolean.TRUE.equals(userExists)) {
					return Mono.error(new AlreadyExistsUserException(ErrorMessage.ALREADY_EXISTS_USER));
				}
				return userRepository.createUser(user);
			});
	}

	private void validateUser(User user) {
		if (user == null) throw new InvalidUserException(ErrorMessage.NULL_USER);

		validateName(user.getName());
		validateLastname(user.getLastname());
		validateEmail(user.getEmail());
		validateSalaryBase(user.getSalaryBase());
	}
	
	private void validateName(String nombres) {
		if (nombres == null || nombres.trim()
			.isEmpty()) {
			throw new InvalidUserException(ErrorMessage.INVALID_USER_NAME);
		}
	}
	
	private void validateLastname(String apellidos) {
		if (apellidos == null || apellidos.trim()
			.isEmpty()) {
			throw new InvalidUserException(ErrorMessage.INVALID_USER_LASTNAME);
		}
	}
	
	private void validateEmail(String correoElectronico) {
		if (correoElectronico == null || correoElectronico.trim()
			.isEmpty()) {
			throw new InvalidUserException(ErrorMessage.NULL_EMAIL);
		}
		if (!correoElectronico.matches(Regex.EMAIL)) {
			throw new InvalidUserException(ErrorMessage.INVALID_EMAIL);
		}
	}
	
	private void validateSalaryBase(BigDecimal salarioBase) {
		if (salarioBase == null || salarioBase.compareTo(SalaryBaseLimits.MIN) < 0 || salarioBase.compareTo(SalaryBaseLimits.MAX) > 0) {
			throw new InvalidUserException(ErrorMessage.INVALID_SALARY_BASE);
		}
	}
}

package com.crediya.usecase.login;

import com.crediya.model.exceptions.user.InvalidLoginException;
import com.crediya.model.user.User;
import com.crediya.model.user.gateways.PasswordEncoderService;
import com.crediya.model.user.gateways.TokenService;
import com.crediya.model.user.gateways.UserRepository;
import com.crediya.model.user.ports.ILoginUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;

import static com.crediya.model.constants.ErrorValidationMessage.*;

@RequiredArgsConstructor
public class LoginUseCase implements ILoginUseCase {
	
	private final PasswordEncoderService passwordEncoderService;
	private final UserRepository userRepository;
	private final TokenService tokenService;
	
	@Override
	public Mono<String> execute(String email, String password) {
		return validateCredentials(email, password)
			.then(Mono.defer(() -> userRepository.findByEmail(email)))
			.filter(Objects::nonNull)
			.switchIfEmpty(Mono.error(new InvalidLoginException(INVALID_LOGIN)))
			.filterWhen(user -> this.matches(password, user.getPassword()))
			.switchIfEmpty(Mono.error(new InvalidLoginException(INVALID_LOGIN)))
			.flatMap(this::generateToken);
	}
	
	private Mono<Void> validateCredentials(String email, String password) {
		if (email == null || email.trim().isEmpty()) {
			return Mono.error(new InvalidLoginException(NULL_EMAIL));
		}
		if (password == null || password.trim().isEmpty()) {
			return Mono.error(new InvalidLoginException(NULL_PASSWORD));
		}
		return Mono.empty();
	}
	
	private Mono<Boolean> matches(String rawPassword, String encodePassword) {
		return Mono.fromCallable(() -> passwordEncoderService.matches(rawPassword, encodePassword))
			.subscribeOn(Schedulers.boundedElastic());
	}
	
	private Mono<String> generateToken(User user) {
		return tokenService.generateToken(user);
	}
}

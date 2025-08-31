package com.crediya.usecase.login;

import com.crediya.model.exceptions.user.InvalidLoginException;
import com.crediya.model.user.User;
import com.crediya.model.user.gateways.PasswordEncoderService;
import com.crediya.model.user.gateways.UserRepository;
import com.crediya.model.user.ports.ILoginUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static com.crediya.model.constants.ErrorMessage.*;

@RequiredArgsConstructor
public class LoginUseCase implements ILoginUseCase {
	
	private final PasswordEncoderService passwordEncoderService;
	private final UserRepository userRepository;
	
	@Override
	public Mono<User> execute(String email, String password) {
		return validateCredentials(email, password)
			.then(userRepository.findByEmail(email))
			.filterWhen(user -> this.matches(password, user.getPassword()))
			.switchIfEmpty(Mono.error(new InvalidLoginException(INVALID_LOGIN)));
	}
	
	private Mono<Void> validateCredentials(String email, String password) {
		return Mono.justOrEmpty(email)
			.filter(e -> !e.trim().isEmpty())
			.switchIfEmpty(Mono.error(new InvalidLoginException(NULL_EMAIL)))
			.then(Mono.justOrEmpty(password)
				.filter(p -> !p.trim().isEmpty())
				.switchIfEmpty(Mono.error(new InvalidLoginException(NULL_PASSWORD))))
			.then();
	}
	
	private Mono<Boolean> matches(String rawPassword, String encodePassword) {
		return Mono.fromCallable(() -> passwordEncoderService.matches(rawPassword, encodePassword))
			.subscribeOn(Schedulers.boundedElastic());
	}
}

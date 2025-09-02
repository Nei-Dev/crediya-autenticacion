package com.crediya.usecase.finduser;

import com.crediya.model.exceptions.user.InvalidUserException;
import com.crediya.model.exceptions.user.UserNotFoundException;
import com.crediya.model.user.User;
import com.crediya.model.user.gateways.UserRepository;
import com.crediya.model.user.ports.IFindUserByIdentificationUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.crediya.model.constants.ErrorMessage.INVALID_IDENTIFICATION;
import static com.crediya.model.constants.ErrorMessage.USER_NOT_FOUND;

@RequiredArgsConstructor
public class FindUserByIdentificationUseCase implements IFindUserByIdentificationUseCase {
	
	private final UserRepository userRepository;
	
	@Override
	public Mono<User> execute(String identification) {
		return Mono.justOrEmpty(identification)
				.filter(Objects::nonNull)
				.filter(idNumber -> !idNumber.trim().isEmpty())
				.switchIfEmpty(Mono.error(new InvalidUserException(INVALID_IDENTIFICATION)))
				.flatMap(userRepository::findByIdentification)
				.switchIfEmpty(Mono.error(new UserNotFoundException(USER_NOT_FOUND)));
	}
}

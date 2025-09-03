package com.crediya.usecase.finduser;

import com.crediya.model.exceptions.user.InvalidUserException;
import com.crediya.model.exceptions.user.UserNotFoundException;
import com.crediya.model.user.User;
import com.crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static com.crediya.model.constants.ErrorValidationMessage.INVALID_IDENTIFICATION;
import static com.crediya.model.constants.ErrorValidationMessage.USER_NOT_FOUND;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserByIdentificationUseCaseTest {
	
	@InjectMocks
	private FindUserByIdentificationUseCase useCase;
	
	@Mock
	private UserRepository userRepository;
	
	private String validIdentification() {
		return "1234567890";
	}
	
	private User validUser() {
		return User.builder()
			.idUser(1L)
			.name("Valid Name")
			.lastname("Valid Lastname")
			.email("mail@example.com")
			.salaryBase(BigDecimal.valueOf(5_000_000))
			.identification(validIdentification())
			.build();
	}
	
	@Test
	void execute_validIdentification_returnsUser() {
		User userRegistered = validUser();
		when(userRepository.findByIdentification(validIdentification()))
			.thenReturn(Mono.just(validUser()));
		StepVerifier.create(useCase.execute(validIdentification()))
			.expectNextMatches(userResult -> userResult.getIdUser().equals(userRegistered.getIdUser()) &&
				userResult.getEmail().equals(userRegistered.getEmail()) &&
				userResult.getIdentification().equals(userRegistered.getIdentification()))
			.verifyComplete();
	}
	
	@Test
	void execute_invalidIdentification_throwsException() {
		StepVerifier.create(useCase.execute(""))
			.expectErrorMatches(e -> e instanceof InvalidUserException &&
				e.getMessage().equals(INVALID_IDENTIFICATION))
			.verify();
		StepVerifier.create(useCase.execute("     "))
			.expectErrorMatches(e -> e instanceof InvalidUserException &&
				e.getMessage().equals(INVALID_IDENTIFICATION))
			.verify();
		StepVerifier.create(useCase.execute(null))
			.expectErrorMatches(e -> e instanceof InvalidUserException &&
				e.getMessage().equals(INVALID_IDENTIFICATION))
			.verify();
	}
	
	@Test
	void execute_userNotFound_throwsException() {
		when(userRepository.findByIdentification(validIdentification()))
			.thenReturn(Mono.empty());
		StepVerifier.create(useCase.execute(validIdentification()))
			.expectErrorMatches(e -> e instanceof UserNotFoundException &&
				e.getMessage().equals(USER_NOT_FOUND))
			.verify();
	}
	
}

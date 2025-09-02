package com.crediya.usecase.login;

import com.crediya.model.exceptions.user.InvalidLoginException;
import com.crediya.model.user.User;
import com.crediya.model.user.UserRole;
import com.crediya.model.user.gateways.PasswordEncoderService;
import com.crediya.model.user.gateways.TokenService;
import com.crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.crediya.model.constants.ErrorMessage.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {
	
	@InjectMocks
	private LoginUseCase useCase;
	
	@Mock
	private UserRepository userRepository;
	@Mock
	private PasswordEncoderService passwordEncoderService;
	@Mock
	private TokenService tokenService;

	private User validUser;

	@BeforeEach
	void setUp() {
		validUser = User.builder()
			.name("Valid Name")
			.lastname("Valid Lastname")
			.email("name.lastname@example.com")
			.password("123456")
			.role(UserRole.CLIENT)
			.build();
	}

	@Test
	void shouldLoginSuccessfully() {
		String email = validUser.getEmail();
		String password = validUser.getPassword();
		String encodedPassword = "encodedPassword";
		String token = "jwt-token";
		User userWithEncodedPassword = User.builder()
			.name(validUser.getName())
			.lastname(validUser.getLastname())
			.email(email)
			.password(encodedPassword)
			.role(UserRole.CLIENT)
			.build();

		when(userRepository.findByEmail(email)).thenReturn(Mono.just(userWithEncodedPassword));
		when(passwordEncoderService.matches(password, encodedPassword)).thenReturn(true);
		when(tokenService.generateToken(userWithEncodedPassword)).thenReturn(token);

		StepVerifier.create(useCase.execute(email, password))
			.expectNext(token)
			.verifyComplete();
	}

	@Test
	void shouldThrowExceptionIfEmailIsNullOrEmpty() {
		StepVerifier.create(useCase.execute(null, validUser.getPassword()))
			.expectErrorMatches(e -> e instanceof InvalidLoginException && e.getMessage().equals(NULL_EMAIL))
			.verify();
		StepVerifier.create(useCase.execute("", validUser.getPassword()))
			.expectErrorMatches(e -> e instanceof InvalidLoginException && e.getMessage().equals(NULL_EMAIL))
			.verify();
	}

	@Test
	void shouldThrowExceptionIfPasswordIsNullOrEmpty() {
		StepVerifier.create(useCase.execute(validUser.getEmail(), null))
			.expectErrorMatches(e -> e instanceof InvalidLoginException && e.getMessage().equals(NULL_PASSWORD))
			.verify();
		StepVerifier.create(useCase.execute(validUser.getEmail(), ""))
			.expectErrorMatches(e -> e instanceof InvalidLoginException && e.getMessage().equals(NULL_PASSWORD))
			.verify();
	}

	@Test
	void shouldThrowExceptionIfUserNotFound() {
		when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Mono.empty());
		StepVerifier.create(useCase.execute(validUser.getEmail(), validUser.getPassword()))
			.expectErrorMatches(e -> e instanceof InvalidLoginException && e.getMessage().equals(INVALID_LOGIN))
			.verify();
	}

	@Test
	void shouldThrowExceptionIfPasswordDoesNotMatch() {
		String email = validUser.getEmail();
		String password = validUser.getPassword();
		String encodedPassword = "encodedPassword";
		User userWithEncodedPassword = User.builder()
			.name(validUser.getName())
			.lastname(validUser.getLastname())
			.email(email)
			.password(encodedPassword)
			.role(UserRole.CLIENT)
			.build();
		when(userRepository.findByEmail(email)).thenReturn(Mono.just(userWithEncodedPassword));
		when(passwordEncoderService.matches(password, encodedPassword)).thenReturn(false);
		StepVerifier.create(useCase.execute(email, password))
			.expectErrorMatches(e -> e instanceof InvalidLoginException && e.getMessage().equals(INVALID_LOGIN))
			.verify();
	}
}

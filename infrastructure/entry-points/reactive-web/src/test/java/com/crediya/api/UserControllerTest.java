package com.crediya.api;

import com.crediya.api.config.UseCaseConfig;
import com.crediya.api.dto.input.user.CreateUserRequest;
import com.crediya.api.dto.input.user.UserApiResponseDTO;
import com.crediya.api.dto.input.user.UserResponseDTO;
import com.crediya.api.dto.output.ErrorResponseDTO;
import com.crediya.model.constants.ErrorMessage;
import com.crediya.model.exceptions.user.AlreadyExistsUserException;
import com.crediya.model.user.User;
import com.crediya.usecase.create_user.CreateUserClientUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.crediya.api.constants.paths.BasePath.USER_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(
	classes = {
		UserController.class,
		UseCaseConfig.class,
		GlobalExceptionHandler.class
	}
)
@WebFluxTest
class UserControllerTest {
	
	@MockBean
	private CreateUserClientUseCase crearUsuarioClienteUseCase;
	
	@Autowired
	private WebTestClient webTestClient;
	
	private CreateUserRequest createUserRequest;
	private User userSaved;
	
	@BeforeEach
	void setUp() {
		String name = "John";
		String lastname = "Doe";
		LocalDate birthDate = LocalDate.of(
			1990,
			1,
			1
		);
		String address = "123 Main St";
		String phone = "555-1234";
		String email = "john.doe@mail.com";
		BigDecimal baseSalary = BigDecimal.valueOf(5_000_000);
		
		createUserRequest = new CreateUserRequest(
			name,
			lastname,
			birthDate,
			address,
			phone,
			email,
			baseSalary
		);
		
		userSaved = User.builder().idUser(1L).name(name).lastname(lastname).birthDate(birthDate).address(address).phone(phone).email(email).salaryBase(baseSalary).build();
	}
	
	@Test
	void createUserClient_ShouldReturnCreatedResponse() {
		
		when(crearUsuarioClienteUseCase.execute(any(User.class))).thenReturn(Mono.just(userSaved));
		
		webTestClient.post()
			.uri(USER_PATH)
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(createUserRequest)
			.exchange()
			.expectStatus()
			.isCreated()
			.expectBody(UserApiResponseDTO.class)
			.value(response -> {
				UserResponseDTO resultResponse = response.getData();
				assertEquals(
					userSaved.getIdUser(),
					resultResponse.id()
				);
				assertEquals(
					userSaved.getName() + " " + userSaved.getLastname(),
					resultResponse.fullname()
				);
			});
	}
	
	@Test
	void createUserClient_ShouldHandleGenericError() {
		
		when(crearUsuarioClienteUseCase.execute(any(User.class)))
			.thenReturn(Mono.error(new RuntimeException("Error creating user")));
		
		webTestClient.post()
			.uri(USER_PATH)
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(createUserRequest)
			.exchange()
			.expectStatus()
			.is5xxServerError();
	}
	
	@Test
	void createUserClient_ShouldHandleBusinessException() {
		
		when(crearUsuarioClienteUseCase.execute(any(User.class)))
			.thenThrow(new AlreadyExistsUserException(ErrorMessage.ALREADY_EXISTS_USER));
		
		webTestClient.post()
			.uri(USER_PATH)
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(createUserRequest)
			.exchange()
			.expectStatus()
			.is4xxClientError()
			.expectBody(ErrorResponseDTO.class)
			.value(response -> {
				assertEquals(ErrorMessage.ALREADY_EXISTS_USER, response.getMensaje());
				assertEquals(400, response.getCodigo());
			});
	}
}

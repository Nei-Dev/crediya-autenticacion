package com.crediya.usecase.create_user;

import com.crediya.model.constants.SalaryBaseLimits;
import com.crediya.model.constants.ErrorMessage;
import com.crediya.model.exceptions.user.InvalidUserException;
import com.crediya.model.user.UserRole;
import com.crediya.model.user.User;
import com.crediya.model.user.gateways.UserRepository;
import com.crediya.model.exceptions.user.AlreadyExistsUserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearUserClienteUseCaseTest {
    
    @InjectMocks
    private CreateUserClientUseCase useCase;
    
    @Mock
    private UserRepository userRepository;

    private User validUser() {
        User user = new User();
        user.setName("Juan");
        user.setLastname("Perez");
        user.setEmail("juan.perez@correo.com");
        user.setSalaryBase(SalaryBaseLimits.MIN.add(BigDecimal.valueOf(1000)));
        return user;
    }

    @Test
    void shouldCreateUserSuccesfully() {
        User user = validUser();
        when(userRepository.findByIdentification(user.getEmail())).thenReturn(Mono.empty());
        when(userRepository.createUser(any(User.class))).thenReturn(Mono.just(user));

        StepVerifier.create(useCase.execute(user))
                .expectNextMatches(u -> u.getEmail().equals(user.getEmail()) && u.getRole() == UserRole.CLIENT)
                .verifyComplete();
    }

    @Test
    void shouldThrowExceptionIfUserAlreadyExists() {
        User user = validUser();
        when(userRepository.findByIdentification(user.getEmail())).thenReturn(Mono.just(user));

        StepVerifier.create(useCase.execute(user))
                .expectErrorMatches(e -> e instanceof AlreadyExistsUserException && e.getMessage().equals(ErrorMessage.ALREADY_EXISTS_USER))
                .verify();
    }

    @Test
    void shouldThrowExceptionIfUserIsNull() {
        StepVerifier.create(useCase.execute(null))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(ErrorMessage.NULL_USER))
                .verify();
    }

    @Test
    void shouldThrowExceptionIfInvalidData() {
        User userWithoutName = validUser();
        userWithoutName.setName("");
        StepVerifier.create(useCase.execute(userWithoutName))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().contains(ErrorMessage.INVALID_USER_NAME))
                .verify();

        User userWithoutLastname = validUser();
        userWithoutLastname.setLastname("");
        StepVerifier.create(useCase.execute(userWithoutLastname))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(ErrorMessage.INVALID_USER_LASTNAME))
                .verify();

        User userInvalidEmail = validUser();
        userInvalidEmail.setEmail(null);
        StepVerifier.create(useCase.execute(userInvalidEmail))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(ErrorMessage.NULL_EMAIL))
                .verify();
        userInvalidEmail.setEmail("invalid-email");
        StepVerifier.create(useCase.execute(userInvalidEmail))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(ErrorMessage.INVALID_EMAIL))
                .verify();

        User userSalarayInvalid = validUser();
        userSalarayInvalid.setSalaryBase(SalaryBaseLimits.MAX.add(BigDecimal.valueOf(1)));
        StepVerifier.create(useCase.execute(userSalarayInvalid))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(ErrorMessage.INVALID_SALARY_BASE))
                .verify();
        
        userSalarayInvalid.setSalaryBase(SalaryBaseLimits.MIN.subtract(BigDecimal.valueOf(1)));
        StepVerifier.create(useCase.execute(userSalarayInvalid))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(ErrorMessage.INVALID_SALARY_BASE))
                .verify();
    }
}


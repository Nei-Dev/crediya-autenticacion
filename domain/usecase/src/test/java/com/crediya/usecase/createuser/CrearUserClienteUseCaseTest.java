package com.crediya.usecase.createuser;

import com.crediya.model.constants.SalaryBaseRules;
import com.crediya.model.exceptions.user.AlreadyExistsUserException;
import com.crediya.model.exceptions.user.InvalidUserException;
import com.crediya.model.user.User;
import com.crediya.model.user.UserRole;
import com.crediya.model.user.gateways.PasswordEncoderService;
import com.crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static com.crediya.model.constants.ErrorValidationMessage.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearUserClienteUseCaseTest {
    
    @InjectMocks
    private CreateUserClientUseCase useCase;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoderService passwordEncoderService;

    private User validUser() {
        return User.builder()
            .name("Valid Name")
            .lastname("Valid Lastname")
            .email("name.lastname@example.com")
            .salaryBase(SalaryBaseRules.MIN.add(BigDecimal.valueOf(1000)))
            .identification("1234567890")
            .password("123456")
            .build();
    }

    @Test
    void shouldCreateUserSuccesfully() {
        User user = validUser();
        String encodedPassword = "encodedPassword";
        User userWithEncodedPassword = User.builder()
            .name(user.getName())
            .lastname(user.getLastname())
            .email(user.getEmail())
            .salaryBase(user.getSalaryBase())
            .identification(user.getIdentification())
            .password(encodedPassword)
            .role(UserRole.CLIENT)
            .build();
        when(userRepository.findByIdentification(user.getIdentification())).thenReturn(Mono.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.empty());
        when(passwordEncoderService.encode(user.getPassword())).thenReturn(encodedPassword);
        when(userRepository.createUser(any(User.class))).thenReturn(Mono.just(userWithEncodedPassword));

        StepVerifier.create(useCase.execute(user))
                .expectNextMatches(u ->
                    u.getName().equals(user.getName())
                    && u.getLastname().equals(user.getLastname())
                    && u.getEmail().equals(user.getEmail())
                    && u.getSalaryBase().equals(user.getSalaryBase())
                    && u.getIdentification().equals(user.getIdentification())
                    && u.getRole() == UserRole.CLIENT
                    && u.getPassword().equals(encodedPassword)
                )
                .verifyComplete();
    }

    @Test
    void shouldThrowExceptionIfUserAlreadyExists() {
        User user = validUser();
        when(userRepository.findByIdentification(user.getIdentification())).thenReturn(Mono.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.just(user));

        StepVerifier.create(useCase.execute(user))
                .expectErrorMatches(e -> e instanceof AlreadyExistsUserException && e.getMessage().equals(ALREADY_EXISTS_USER))
                .verify();
        
        when(userRepository.findByIdentification(user.getIdentification())).thenReturn(Mono.just(user));
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(user))
                .expectErrorMatches(e -> e instanceof AlreadyExistsUserException && e.getMessage().equals(ALREADY_EXISTS_USER))
                .verify();
    }

    @Test
    void shouldThrowExceptionIfUserIsNull() {
        StepVerifier.create(useCase.execute(null))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(NULL_USER))
                .verify();
    }

    @Test
    void shouldThrowExceptionIfInvalidData() {
        User userWithoutName = validUser();
        userWithoutName.setName("");
        StepVerifier.create(useCase.execute(userWithoutName))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().contains(INVALID_USER_NAME))
                .verify();
        userWithoutName.setName(null);
        StepVerifier.create(useCase.execute(userWithoutName))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().contains(INVALID_USER_NAME))
                .verify();

        User userWithoutLastname = validUser();
        userWithoutLastname.setLastname("");
        StepVerifier.create(useCase.execute(userWithoutLastname))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(INVALID_USER_LASTNAME))
                .verify();
        userWithoutLastname.setLastname(null);
        StepVerifier.create(useCase.execute(userWithoutLastname))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(INVALID_USER_LASTNAME))
                .verify();
        
        User userWithoutIdentification = validUser();
        userWithoutIdentification.setIdentification("");
        StepVerifier.create(useCase.execute(userWithoutIdentification))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(IDENTIFICATION_NOT_BLANK))
                .verify();
        userWithoutIdentification.setIdentification(null);
        StepVerifier.create(useCase.execute(userWithoutIdentification))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(IDENTIFICATION_NOT_BLANK))
                .verify();
        userWithoutIdentification.setIdentification("invalid-id");
        StepVerifier.create(useCase.execute(userWithoutIdentification))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(INVALID_IDENTIFICATION))
                .verify();

        User userInvalidEmail = validUser();
        userInvalidEmail.setEmail(null);
        StepVerifier.create(useCase.execute(userInvalidEmail))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(NULL_EMAIL))
                .verify();
        userInvalidEmail.setEmail("");
        StepVerifier.create(useCase.execute(userInvalidEmail))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(NULL_EMAIL))
                .verify();
        userInvalidEmail.setEmail("invalid-email");
        StepVerifier.create(useCase.execute(userInvalidEmail))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(INVALID_EMAIL))
                .verify();

        User userSalarayInvalid = validUser();
        userSalarayInvalid.setSalaryBase(null);
        StepVerifier.create(useCase.execute(userSalarayInvalid))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(INVALID_SALARY_BASE))
                .verify();
        
        userSalarayInvalid.setSalaryBase(SalaryBaseRules.MAX.add(BigDecimal.valueOf(1)));
        StepVerifier.create(useCase.execute(userSalarayInvalid))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(INVALID_SALARY_BASE))
                .verify();
        
        userSalarayInvalid.setSalaryBase(SalaryBaseRules.MIN.subtract(BigDecimal.valueOf(1)));
        StepVerifier.create(useCase.execute(userSalarayInvalid))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(INVALID_SALARY_BASE))
                .verify();
        
        User userInvalidPassword = validUser();
        userInvalidPassword.setPassword(null);
        StepVerifier.create(useCase.execute(userInvalidPassword))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(NULL_PASSWORD))
                .verify();
        userInvalidPassword.setPassword("");
        StepVerifier.create(useCase.execute(userInvalidPassword))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(NULL_PASSWORD))
                .verify();
        userInvalidPassword.setPassword("123");
        StepVerifier.create(useCase.execute(userInvalidPassword))
                .expectErrorMatches(e -> e instanceof InvalidUserException && e.getMessage().equals(INVALID_PASSWORD))
                .verify();
    }
}


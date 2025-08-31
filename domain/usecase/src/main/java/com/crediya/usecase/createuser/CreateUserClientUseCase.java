package com.crediya.usecase.createuser;

import com.crediya.model.constants.Regex;
import com.crediya.model.constants.SalaryBaseRules;
import com.crediya.model.exceptions.user.AlreadyExistsUserException;
import com.crediya.model.exceptions.user.InvalidUserException;
import com.crediya.model.user.User;
import com.crediya.model.user.UserRole;
import com.crediya.model.user.gateways.PasswordEncoderService;
import com.crediya.model.user.gateways.UserRepository;
import com.crediya.model.user.ports.ICreateUserClientUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.crediya.model.constants.ErrorMessage.*;
import static com.crediya.model.constants.PasswordRules.MIN_LENGTH;

@RequiredArgsConstructor
public class CreateUserClientUseCase implements ICreateUserClientUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;

    @Override
    public Mono<User> execute(User user) {
        return validateUser(user)
                .flatMap(this::checkIfUserExists)
                .flatMap(this::setDefaultRole)
                .flatMap(this::encodePassword)
                .flatMap(userRepository::createUser);
    }
    
    private Mono<User> validateUser(User user) {
        return Mono.justOrEmpty(user)
            .filter(Objects::nonNull)
            .switchIfEmpty(Mono.error(new InvalidUserException(NULL_USER)))
            .flatMap(this::validateFirstName)
            .flatMap(this::validateLastname)
            .flatMap(this::validateEmail)
            .flatMap(this::validateSalaryBase)
            .flatMap(this::validateIdentification)
            .flatMap(this::validatePassword);
    }
    
    private Mono<User> validateFirstName(User user) {
        return Mono.just(user)
            .filter(u -> u.getName() != null && !u.getName().trim().isEmpty())
            .switchIfEmpty(Mono.error(new InvalidUserException(INVALID_USER_NAME)));
    }
    
    private Mono<User> validateLastname(User user) {
        return Mono.just(user)
            .filter(u -> u.getLastname() != null && !u.getLastname().trim().isEmpty())
            .switchIfEmpty(Mono.error(new InvalidUserException(INVALID_USER_LASTNAME)));
    }
    
    private Mono<User> validateEmail(User user) {
        return Mono.just(user)
            .filter(u -> u.getEmail() != null && !u.getEmail().trim().isEmpty())
            .switchIfEmpty(Mono.error(new InvalidUserException(NULL_EMAIL)))
            .filter(u -> u.getEmail().matches(Regex.EMAIL))
            .switchIfEmpty(Mono.error(new InvalidUserException(INVALID_EMAIL)));
    }
    
    private Mono<User> validateSalaryBase(User user) {
        return Mono.just(user)
            .filter(
                u -> u.getSalaryBase() != null
                && u.getSalaryBase().compareTo(SalaryBaseRules.MIN) > 0
                && u.getSalaryBase().compareTo(SalaryBaseRules.MAX) < 0
            )
            .switchIfEmpty(Mono.error(new InvalidUserException(INVALID_SALARY_BASE)));
    }
    
    private Mono<User> validateIdentification(User user) {
        return Mono.just(user)
            .filter(u -> u.getIdentification() != null && !u.getIdentification().trim().isEmpty())
            .switchIfEmpty(Mono.error(new InvalidUserException(IDENTIFICATION_NOT_BLANK)))
            .filter(u -> u.getIdentification().matches(Regex.IDENTIFICATION))
            .switchIfEmpty(Mono.error(new InvalidUserException(INVALID_IDENTIFICATION)));
    }
    
    private Mono<User> validatePassword(User user) {
        return Mono.just(user)
            .filter(u -> u.getPassword() != null && !u.getPassword().trim().isEmpty())
            .switchIfEmpty(Mono.error(new InvalidUserException(NULL_PASSWORD)))
            .filter(u -> u.getPassword().length() >= MIN_LENGTH)
            .switchIfEmpty(Mono.error(new InvalidUserException(INVALID_PASSWORD)));
    }
        
    private Mono<User> checkIfUserExists(User user) {
        return Mono.zip(
            userRepository.findByEmail(user.getEmail()).hasElement(),
            userRepository.findByIdentification(user.getIdentification()).hasElement()
        ).flatMap(results -> {
            boolean existByEmail = results.getT1();
            boolean existByIdentification = results.getT2();
            if (existByEmail || existByIdentification) {
                return Mono.error(new AlreadyExistsUserException(ALREADY_EXISTS_USER));
            }
            return Mono.just(user);
        });
    }
    
    private Mono<User> setDefaultRole(User user) {
        user.setRole(UserRole.CLIENT);
        return Mono.just(user);
    }
    
    private Mono<User> encodePassword(User user) {
        String encodedPassword = passwordEncoderService.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return Mono.just(user);
    }
}

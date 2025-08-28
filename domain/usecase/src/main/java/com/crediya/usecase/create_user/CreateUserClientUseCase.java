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

@RequiredArgsConstructor
public class CreateUserClientUseCase implements ICreateUserClientUseCase {

    private final UserRepository userRepository;

    @Override
    public Mono<User> execute(User user) {
        return validateUser(user)
                .flatMap(this::checkIfUserExists)
                .flatMap(this::setDefaultRole)
                .flatMap(userRepository::createUser);
    }
    
    private Mono<User> validateUser(User user) {
        return Mono.justOrEmpty(user)
                .switchIfEmpty(Mono.error(new InvalidUserException(ErrorMessage.NULL_USER)))
                .filter(u -> u.getName() != null
                    && !u.getName().trim().isEmpty())
                .switchIfEmpty(Mono.error(new InvalidUserException(ErrorMessage.INVALID_USER_NAME)))
                .filter(u -> u.getLastname() != null
                    && !u.getLastname().trim().isEmpty())
                .switchIfEmpty(Mono.error(new InvalidUserException(ErrorMessage.INVALID_USER_LASTNAME)))
                .filter(u -> u.getEmail() != null
                    && !u.getEmail().trim().isEmpty())
                .switchIfEmpty(Mono.error(new InvalidUserException(ErrorMessage.NULL_EMAIL)))
                .filter(u -> u.getEmail().matches(Regex.EMAIL))
                .switchIfEmpty(Mono.error(new InvalidUserException(ErrorMessage.INVALID_EMAIL)))
                .filter(u -> u.getSalaryBase() != null
                    && u.getSalaryBase().compareTo(SalaryBaseLimits.MIN) >= 0
                    && u.getSalaryBase().compareTo(SalaryBaseLimits.MAX) <= 0)
                .switchIfEmpty(Mono.error(new InvalidUserException(ErrorMessage.INVALID_SALARY_BASE)))
                .filter(u -> u.getIdentification() != null
                    && !u.getIdentification().trim().isEmpty())
                .switchIfEmpty(Mono.error(new InvalidUserException(ErrorMessage.IDENTIFICATION_NOT_BLANK)))
                .filter(u -> u.getIdentification().matches(Regex.IDENTIFICATION))
                .switchIfEmpty(Mono.error(new InvalidUserException(ErrorMessage.INVALID_IDENTIFICATION)));
    }
    
    private Mono<User> checkIfUserExists(User user) {
        return Mono.zip(
            userRepository.findByEmail(user.getEmail()).hasElement(),
            userRepository.findByIdentification(user.getIdentification()).hasElement()
        ).flatMap(results -> {
            boolean existByEmail = results.getT1();
            boolean existByIdentification = results.getT2();
            if (existByEmail || existByIdentification) {
                return Mono.error(new AlreadyExistsUserException(ErrorMessage.ALREADY_EXISTS_USER));
            }
            return Mono.just(user);
        });
    }
    
    private Mono<User> setDefaultRole(User user) {
        user.setRole(UserRole.CLIENT);
        return Mono.just(user);
    }
}

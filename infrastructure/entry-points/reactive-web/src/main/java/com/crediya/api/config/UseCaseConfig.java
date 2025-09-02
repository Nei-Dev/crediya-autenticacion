package com.crediya.api.config;

import com.crediya.model.user.gateways.PasswordEncoderService;
import com.crediya.model.user.gateways.TokenService;
import com.crediya.model.user.gateways.UserRepository;
import com.crediya.model.user.ports.ICreateUserClientUseCase;
import com.crediya.model.user.ports.IFindUserByIdentificationUseCase;
import com.crediya.model.user.ports.ILoginUseCase;
import com.crediya.usecase.createuser.CreateUserClientUseCase;
import com.crediya.usecase.finduser.FindUserByIdentificationUseCase;
import com.crediya.usecase.login.LoginUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
	
	@Bean
	public ICreateUserClientUseCase createUserClientUseCase(UserRepository userRepository, PasswordEncoderService passwordEncoderService) {
		return new CreateUserClientUseCase(userRepository, passwordEncoderService);
	}
	
	@Bean
	public IFindUserByIdentificationUseCase findUserByIdentificationUseCase(UserRepository userRepository){
		return new FindUserByIdentificationUseCase(userRepository);
	}
	
	@Bean
	public ILoginUseCase loginUseCase(PasswordEncoderService passwordEncoderService, UserRepository userRepository, TokenService tokenService) {
		return new LoginUseCase(passwordEncoderService, userRepository, tokenService);
	}
}

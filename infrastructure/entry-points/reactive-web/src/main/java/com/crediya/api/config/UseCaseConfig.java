package com.crediya.api.config;

import com.crediya.model.user.gateways.UserRepository;
import com.crediya.model.user.ports.ICreateUserClientUseCase;
import com.crediya.model.user.ports.IFindUserByIdentificationUseCase;
import com.crediya.usecase.createuser.CreateUserClientUseCase;
import com.crediya.usecase.finduser.FindUserByIdentificationUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
	
	@Bean
	public ICreateUserClientUseCase createUserClientUseCase(UserRepository userRepository) {
		return new CreateUserClientUseCase(userRepository);
	}
	
	@Bean
	public IFindUserByIdentificationUseCase findUserByIdentificationUseCase(UserRepository userRepository){
		return new FindUserByIdentificationUseCase(userRepository);
	}
}

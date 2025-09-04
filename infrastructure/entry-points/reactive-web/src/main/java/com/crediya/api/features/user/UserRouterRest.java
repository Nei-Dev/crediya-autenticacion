package com.crediya.api.features.user;

import com.crediya.api.constants.paths.UserPath;
import com.crediya.api.openapi.UserDocApi;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Configuration
@RequiredArgsConstructor
public class UserRouterRest {
	
	private final UserPath userPath;
	
	@Bean
	public RouterFunction<ServerResponse> userRouterFunction(UserHandler handler) {
		return route()
			.POST(
				userPath.getCreateUser(),
				handler::createUserClient,
				UserDocApi::createUserClientDoc
			)
			.GET(
				userPath.getFindUserByIdentification(),
				handler::findUserByIdentification,
				UserDocApi::findUserByIdentificationDoc
			)
			.build();
	}
}

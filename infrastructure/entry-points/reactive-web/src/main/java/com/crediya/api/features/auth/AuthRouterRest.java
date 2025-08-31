package com.crediya.api.features.auth;

import com.crediya.api.constants.paths.AuthPath;
import com.crediya.api.helpers.ApiDocHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Configuration
@RequiredArgsConstructor
public class AuthRouterRest {
	
	private final AuthPath authPath;
	
	@Bean
	public RouterFunction<ServerResponse> authRouterFunction(AuthHandler handler) {
		return route()
			.POST(
				authPath.getLogin(),
				handler::login,
				builder -> ApiDocHelper.commonErrorResponse(
					builder
				)
			)
			.build();
	}
}

package com.crediya.api;

import com.crediya.api.config.swagger.ApiDocHelper;
import com.crediya.api.config.swagger.AuthApiConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.crediya.api.constants.paths.UserPath.SEARCH_BY_IDENTIFICATION;
import static com.crediya.api.constants.paths.UserPath.USER_PATH;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Configuration
public class RouterRest {
	
	@Bean
	public RouterFunction<ServerResponse> routerFunction(AuthHandler handler) {
		return route()
			.POST(USER_PATH,
				handler::createUserClient,
				builder -> ApiDocHelper.commonErrorResponse(
					AuthApiConfig.createUserClientDoc(builder)
				)
			)
			.GET(SEARCH_BY_IDENTIFICATION,
				handler::findUserByIdentification,
				builder -> ApiDocHelper.commonErrorResponse(
					AuthApiConfig.findUserByIdentificationDoc(builder)
				)
			)
			.build();
	}
}

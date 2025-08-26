package com.crediya.api.config;

import com.crediya.api.constants.swagger.DocApi;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
	
	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI().info(new Info().title(DocApi.TITLE)
			.description(DocApi.DESCRIPTION)
			.version(DocApi.VERSION));
	}
}

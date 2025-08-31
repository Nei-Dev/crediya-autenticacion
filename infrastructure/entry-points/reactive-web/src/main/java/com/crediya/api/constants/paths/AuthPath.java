package com.crediya.api.constants.paths;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.paths.auth")
public class AuthPath {
	
	private String login;
	
}

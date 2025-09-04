package com.crediya.api.constants.paths;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.paths.user")
public class UserPath {
	
	private String createUser;
	private String findUserByIdentification;
	
}

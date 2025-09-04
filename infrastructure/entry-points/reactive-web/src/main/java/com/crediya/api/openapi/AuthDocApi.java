package com.crediya.api.openapi;

import com.crediya.api.dto.input.auth.LoginRequest;
import com.crediya.api.dto.output.auth.AuthResponse;
import com.crediya.api.dto.output.user.UserResponse;
import com.crediya.api.helpers.ApiDocHelper;
import lombok.experimental.UtilityClass;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

@UtilityClass
public class AuthDocApi {

	public static final String TAG_AUTH = "Authentication";
	
	// Login
	public static final String OPERATION_ID_LOGIN = "login";
	public static final String SUMMARY_LOGIN = "Authenticate a user and obtain a JWT token";
	public static final String DESCRIPTION_LOGIN = "User authenticated successfully";
	
	// Find User Me by token
	public static final String OPERATION_ID_FIND_ME = "findMe";
	public static final String SUMMARY_FIND_ME = "Find a user by token";
	public static final String DESCRIPTION_FIND_ME = "User found successfully";
	
	public void loginDoc(Builder builder) {
		ApiDocHelper.commonErrorResponse(
			builder
				.summary(SUMMARY_LOGIN)
				.operationId(OPERATION_ID_LOGIN)
				.tag(TAG_AUTH)
				.requestBody(requestBodyBuilder()
					.required(true)
					.content(contentBuilder()
						.mediaType(MediaType.APPLICATION_JSON_VALUE)
						.schema(schemaBuilder()
							.implementation(LoginRequest.class)
						)
					)
				)
				.response(responseBuilder()
					.responseCode(String.valueOf(HttpStatus.OK.value()))
					.description(DESCRIPTION_LOGIN)
					.content(contentBuilder()
						.mediaType(MediaType.APPLICATION_JSON_VALUE)
						.schema(schemaBuilder()
							.implementation(AuthResponse.class)
						)
					)
				)
		);
	}
	
	public void findUserMeDoc(Builder builder) {
		ApiDocHelper.commonErrorResponse(
			builder
				.summary(SUMMARY_FIND_ME)
				.operationId(OPERATION_ID_FIND_ME)
				.tag(TAG_AUTH)
				.response(responseBuilder()
					.responseCode(String.valueOf(HttpStatus.OK.value()))
					.description(DESCRIPTION_FIND_ME)
					.content(contentBuilder()
						.mediaType(MediaType.APPLICATION_JSON_VALUE)
						.schema(schemaBuilder()
							.implementation(UserResponse.class)
						)
					)
				)
		);
	}

}

package com.crediya.api.config.swagger;

import com.crediya.api.dto.input.user.CreateUserRequest;
import com.crediya.api.dto.output.user.UserApiResponseDTO;
import com.crediya.api.dto.output.user.UserResponseDTO;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.crediya.api.constants.swagger.user.UserDocApi.*;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

public class AuthApiConfig {
	
	private AuthApiConfig() {}
	
	public static Builder createUserClientDoc(Builder builder) {
		return builder
			.summary(SUMMARY_CREATE)
			.operationId(OPERATION_ID_CREATE)
			.tag(TAG_USER)
			.requestBody(requestBodyBuilder()
				.required(true)
				.content(contentBuilder()
					.mediaType(MediaType.APPLICATION_JSON_VALUE)
					.schema(schemaBuilder()
						.implementation(CreateUserRequest.class)
					)
				)
			)
			.response(responseBuilder()
				.responseCode(String.valueOf(HttpStatus.CREATED.value()))
				.description(DESCRIPTION_CREATED)
				.content(contentBuilder()
					.mediaType(MediaType.APPLICATION_JSON_VALUE)
					.schema(schemaBuilder()
						.implementation(UserApiResponseDTO.class)
					)
				)
			);
	}
	
	public static Builder findUserByIdentificationDoc(Builder builder) {
		return builder
			.summary(SUMMARY_FIND_BY_IDENTIFICATION)
			.operationId(OPERATION_ID_FIND_BY_IDENTIFICATION)
			.tag(TAG_USER)
			.response(responseBuilder()
				.responseCode(String.valueOf(HttpStatus.OK.value()))
				.description(DESCRIPTION_FIND_BY_IDENTIFICATION)
				.content(contentBuilder()
					.mediaType(MediaType.APPLICATION_JSON_VALUE)
					.schema(schemaBuilder()
						.implementation(UserResponseDTO.class)
					)
				)
			);
	}
}

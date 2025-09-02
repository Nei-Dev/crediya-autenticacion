package com.crediya.api.openapi;

import com.crediya.api.dto.input.user.CreateUserRequest;
import com.crediya.api.dto.output.user.UserApiResponse;
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
public class UserDocApi {
	
	public final String TAG_USER = "User";
	
	// Create User
	public final String OPERATION_ID_CREATE = "createUser";
	public final String SUMMARY_CREATE = "Create a new user in the system";
	public final String DESCRIPTION_CREATED = "User created successfully";
	
	// Find User by Identification
	public final String OPERATION_ID_FIND_BY_IDENTIFICATION = "findUserByIdentification";
	public final String SUMMARY_FIND_BY_IDENTIFICATION = "Find a user by their identification number";
	public final String DESCRIPTION_FIND_BY_IDENTIFICATION = "User found successfully";
	
	public void createUserClientDoc(Builder builder) {
		ApiDocHelper.commonErrorResponse(
			builder
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
							.implementation(UserApiResponse.class)
						)
					)
				)
		);
	}
	
	public void findUserByIdentificationDoc(Builder builder) {
		ApiDocHelper.commonErrorResponse(
			builder
				.summary(SUMMARY_FIND_BY_IDENTIFICATION)
				.operationId(OPERATION_ID_FIND_BY_IDENTIFICATION)
				.tag(TAG_USER)
				.response(responseBuilder()
					.responseCode(String.valueOf(HttpStatus.OK.value()))
					.description(DESCRIPTION_FIND_BY_IDENTIFICATION)
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

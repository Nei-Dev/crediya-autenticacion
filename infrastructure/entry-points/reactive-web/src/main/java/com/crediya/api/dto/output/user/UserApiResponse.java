package com.crediya.api.dto.output.user;

import com.crediya.api.dto.output.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserApiResponse extends ApiResponse<UserResponse> {
	
	public UserApiResponse(UserResponse data, String message) {
		super(data, message);
	}
	
	public static UserApiResponse of(UserResponse data, String message) {
		return new UserApiResponse(data, message);
	}
}

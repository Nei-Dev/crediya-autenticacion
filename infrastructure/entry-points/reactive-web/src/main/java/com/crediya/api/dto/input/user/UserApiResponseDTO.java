package com.crediya.api.dto.input.user;

import com.crediya.api.dto.input.ApiResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserApiResponseDTO extends ApiResponseDTO<UserResponseDTO> {
	
	public UserApiResponseDTO(UserResponseDTO data, String message) {
		super(data, message);
	}
	
	public static UserApiResponseDTO of(UserResponseDTO data, String message) {
		return new UserApiResponseDTO(data, message);
	}
}

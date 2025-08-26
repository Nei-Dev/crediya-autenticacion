package com.crediya.api.dto.input.user;

import com.crediya.api.dto.input.ApiResponseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserApiResponseDTO extends ApiResponseDTO<UserResponseDTO> {
	
	public static UserApiResponseDTO of(UserResponseDTO data, String message) {
		return (UserApiResponseDTO) ApiResponseDTO.of(data, message);
	}
}

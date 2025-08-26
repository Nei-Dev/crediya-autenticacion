package com.crediya.api.dto.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO<T> {
	private T data;
	private String message;
	
	public static <T> ApiResponseDTO<T> of(T data, String message) {
		return ApiResponseDTO.<T>builder()
				.data(data)
				.message(message)
				.build();
	}
}

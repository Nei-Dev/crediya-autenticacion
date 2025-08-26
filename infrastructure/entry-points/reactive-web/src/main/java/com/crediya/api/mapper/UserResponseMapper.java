package com.crediya.api.mapper;

import com.crediya.api.dto.input.user.UserResponseDTO;
import com.crediya.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserResponseMapper {
	
	UserResponseMapper INSTANCE = Mappers.getMapper(UserResponseMapper.class);
	
	@Mapping(target = "fullname", expression = "java(user.getName() + \" \" + user.getLastname())")
	UserResponseDTO toUserResponse(User user);
}

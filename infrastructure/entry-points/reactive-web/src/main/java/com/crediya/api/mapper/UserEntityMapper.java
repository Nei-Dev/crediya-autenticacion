package com.crediya.api.mapper;

import com.crediya.api.dto.input.user.CreateUserRequest;
import com.crediya.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserEntityMapper {
	
	UserEntityMapper INSTANCE = Mappers.getMapper(UserEntityMapper.class);
	
	@Mapping(target = "idUser", ignore = true)
	User toUser(CreateUserRequest createUserRequest);
}

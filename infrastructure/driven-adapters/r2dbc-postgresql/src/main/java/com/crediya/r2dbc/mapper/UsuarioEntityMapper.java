package com.crediya.r2dbc.mapper;

import com.crediya.model.user.User;
import com.crediya.r2dbc.entities.UserData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UsuarioEntityMapper {
	
	UsuarioEntityMapper INSTANCE = Mappers.getMapper(UsuarioEntityMapper.class);

	@Mapping(target = "role", ignore = true)
	@Mapping(target = "idUser", source = "id")
	User toEntity(UserData data);
	
	@Mapping(target = "id", source = "idUser")
	@Mapping(target = "idRole", ignore = true)
	UserData toData(User entity);
	
}


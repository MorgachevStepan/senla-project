package com.stepanew.senlaproject.domain.mapper.user;

import com.stepanew.senlaproject.domain.dto.request.UserCreateRequestDto;
import com.stepanew.senlaproject.domain.entity.User;
import com.stepanew.senlaproject.domain.mapper.EntityRequestMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserCreateRequestDtoMapper extends EntityRequestMapper<UserCreateRequestDto, User> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "profile", ignore = true)
    User toEntity(UserCreateRequestDto dto);

    @Override
    UserCreateRequestDto toDto(User entity);

    @Override
    List<User> toEntity(List<UserCreateRequestDto> dtoList);

    @Override
    List<UserCreateRequestDto> toDto(List<User> entityList);

}

package com.stepanew.senlaproject.domain.mapper.user;

import com.stepanew.senlaproject.domain.dto.response.UserCreatedResponseDto;
import com.stepanew.senlaproject.domain.entity.User;
import com.stepanew.senlaproject.domain.mapper.EntityRequestMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserCreateResponseDtoMapper extends EntityRequestMapper<UserCreatedResponseDto, User> {

    @Override
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "profile.firstName", target = "firstName"),
            @Mapping(source = "profile.lastName", target = "lastName"),
            @Mapping(source = "profile.patronymic", target = "patronymic")
    })
    UserCreatedResponseDto toDto(User entity);

}

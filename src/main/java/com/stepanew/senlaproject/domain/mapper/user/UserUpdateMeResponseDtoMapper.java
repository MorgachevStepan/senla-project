package com.stepanew.senlaproject.domain.mapper.user;

import com.stepanew.senlaproject.domain.dto.response.UserUpdateMeResponseDto;
import com.stepanew.senlaproject.domain.entity.User;
import com.stepanew.senlaproject.domain.mapper.EntityResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserUpdateMeResponseDtoMapper extends EntityResponseMapper<UserUpdateMeResponseDto, User> {

    @Override
    User toEntity(UserUpdateMeResponseDto dto);

    @Override
    @Mappings({
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "profile.firstName", target = "firstName"),
            @Mapping(source = "profile.lastName", target = "lastName"),
            @Mapping(source = "profile.patronymic", target = "patronymic")
    })
    UserUpdateMeResponseDto toDto(User entity);

}

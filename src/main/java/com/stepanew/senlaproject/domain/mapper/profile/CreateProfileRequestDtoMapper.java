package com.stepanew.senlaproject.domain.mapper.profile;

import com.stepanew.senlaproject.domain.dto.request.UserCreateRequestDto;
import com.stepanew.senlaproject.domain.entity.Profile;
import com.stepanew.senlaproject.domain.mapper.EntityRequestMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CreateProfileRequestDtoMapper extends EntityRequestMapper<UserCreateRequestDto, Profile> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Profile toEntity(UserCreateRequestDto dto);

    @Override
    UserCreateRequestDto toDto(Profile entity);

    @Override
    List<Profile> toEntity(List<UserCreateRequestDto> dtoList);

    @Override
    List<UserCreateRequestDto> toDto(List<Profile> entityList);

}

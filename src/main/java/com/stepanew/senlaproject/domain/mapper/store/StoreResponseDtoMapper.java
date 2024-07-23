package com.stepanew.senlaproject.domain.mapper.store;

import com.stepanew.senlaproject.domain.dto.response.StoreResponseDto;
import com.stepanew.senlaproject.domain.entity.Store;
import com.stepanew.senlaproject.domain.mapper.EntityResponseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StoreResponseDtoMapper extends EntityResponseMapper<StoreResponseDto, Store> {
}

package com.stepanew.senlaproject.domain.mapper.store;

import com.stepanew.senlaproject.domain.dto.request.StoreCreateRequestDto;
import com.stepanew.senlaproject.domain.entity.Store;
import com.stepanew.senlaproject.domain.mapper.EntityRequestMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StoreCreateRequestDtoMapper extends EntityRequestMapper<StoreCreateRequestDto, Store> {
}

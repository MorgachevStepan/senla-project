package com.stepanew.senlaproject.domain.mapper.price;

import com.stepanew.senlaproject.domain.dto.request.PriceCreateRequestDto;
import com.stepanew.senlaproject.domain.entity.Price;
import com.stepanew.senlaproject.domain.mapper.EntityRequestMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceCreateRequestDtoMapper extends EntityRequestMapper<PriceCreateRequestDto, Price> {
}

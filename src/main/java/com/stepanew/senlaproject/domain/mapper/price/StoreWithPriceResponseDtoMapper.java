package com.stepanew.senlaproject.domain.mapper.price;

import com.stepanew.senlaproject.domain.dto.response.StoreWithPriceResponseDto;
import com.stepanew.senlaproject.domain.entity.Price;
import com.stepanew.senlaproject.domain.mapper.EntityResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface StoreWithPriceResponseDtoMapper extends EntityResponseMapper<StoreWithPriceResponseDto, Price> {

    @Mappings({
            @Mapping(source = "price.store.id", target = "id"),
            @Mapping(source = "price.store.name", target = "name"),
            @Mapping(source = "price.store.address", target = "address"),
            @Mapping(source = "price.price", target = "price")
    })
    StoreWithPriceResponseDto toDto(Price price);

}

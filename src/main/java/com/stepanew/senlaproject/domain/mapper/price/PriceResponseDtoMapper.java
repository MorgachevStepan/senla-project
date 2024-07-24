package com.stepanew.senlaproject.domain.mapper.price;

import com.stepanew.senlaproject.domain.dto.response.PriceResponseDto;
import com.stepanew.senlaproject.domain.entity.Price;
import com.stepanew.senlaproject.domain.mapper.EntityResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PriceResponseDtoMapper extends EntityResponseMapper<PriceResponseDto, Price> {

    @Mappings({
            @Mapping(source = "product.id", target = "productId"),
            @Mapping(source = "store.id", target = "storeId"),
            @Mapping(source = "price.price", target = "price"),
            @Mapping(source = "price.checkedDate", target = "checkedDate")
    })
    PriceResponseDto toDto(Price price);

    @Mappings({
            @Mapping(source = "productId", target = "product.id"),
            @Mapping(source = "storeId", target = "store.id"),
            @Mapping(source = "price", target = "price"),
            @Mapping(source = "checkedDate", target = "checkedDate")
    })
    Price toEntity(PriceResponseDto dto);


}

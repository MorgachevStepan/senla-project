package com.stepanew.senlaproject.domain.mapper.product;

import com.stepanew.senlaproject.domain.dto.response.ProductResponseDto;
import com.stepanew.senlaproject.domain.entity.Product;
import com.stepanew.senlaproject.domain.mapper.EntityResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductResponseDtoMapper extends EntityResponseMapper<ProductResponseDto, Product> {

    @Mapping(source = "category.name", target = "category")
    ProductResponseDto toDto(Product product);

    @Mapping(source = "category", target = "category.name")
    Product toEntity(ProductResponseDto dto);

}

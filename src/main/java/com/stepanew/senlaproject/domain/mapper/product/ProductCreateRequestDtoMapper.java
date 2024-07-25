package com.stepanew.senlaproject.domain.mapper.product;

import com.stepanew.senlaproject.domain.dto.request.ProductCreateRequestDto;
import com.stepanew.senlaproject.domain.entity.Product;
import com.stepanew.senlaproject.domain.mapper.EntityRequestMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProductCreateRequestDtoMapper extends EntityRequestMapper<ProductCreateRequestDto, Product> {

    @Mappings({
            @Mapping(source = "category.id", target = "categoryId"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description")
    })
    ProductCreateRequestDto toDto(Product product);

}

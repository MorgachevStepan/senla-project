package com.stepanew.senlaproject.domain.mapper.product;

import com.stepanew.senlaproject.domain.dto.request.ProductCreateRequestDto;
import com.stepanew.senlaproject.domain.entity.Product;
import com.stepanew.senlaproject.domain.mapper.EntityRequestMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductCreateRequestDtoMapper extends EntityRequestMapper<ProductCreateRequestDto, Product> {
}

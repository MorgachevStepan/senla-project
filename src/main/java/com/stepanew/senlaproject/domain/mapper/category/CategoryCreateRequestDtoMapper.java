package com.stepanew.senlaproject.domain.mapper.category;

import com.stepanew.senlaproject.domain.dto.request.CategoryCreateRequestDto;
import com.stepanew.senlaproject.domain.entity.Category;
import com.stepanew.senlaproject.domain.mapper.EntityRequestMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryCreateRequestDtoMapper extends EntityRequestMapper<CategoryCreateRequestDto, Category> {
}

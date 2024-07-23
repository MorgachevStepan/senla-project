package com.stepanew.senlaproject.domain.mapper.category;

import com.stepanew.senlaproject.domain.dto.response.CategoryResponseDto;
import com.stepanew.senlaproject.domain.entity.Category;
import com.stepanew.senlaproject.domain.mapper.EntityResponseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryResponseDtoMapper extends EntityResponseMapper<CategoryResponseDto, Category> {
}

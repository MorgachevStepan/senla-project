package com.stepanew.senlaproject.services;

import com.stepanew.senlaproject.domain.dto.request.CategoryCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.CategoryUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.CategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryResponseDto findById(Long id);

    Page<CategoryResponseDto> findAll(Pageable pageable, String name);

    void delete(Long id);

    CategoryResponseDto create(CategoryCreateRequestDto request);

    CategoryResponseDto update(CategoryUpdateRequestDto request);

}

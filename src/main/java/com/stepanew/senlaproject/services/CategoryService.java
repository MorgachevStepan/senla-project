package com.stepanew.senlaproject.services;

import com.stepanew.senlaproject.domain.dto.request.CategoryCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.CategoryUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.CategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface CategoryService {

    @Transactional(readOnly = true)
    CategoryResponseDto findById(Long id);

    @Transactional(readOnly = true)
    Page<CategoryResponseDto> findAll(Pageable pageable, String name);

    @Transactional(readOnly = false)
    void delete(Long id);

    @Transactional(readOnly = false)
    CategoryResponseDto create(CategoryCreateRequestDto request);

    @Transactional(readOnly = false)
    CategoryResponseDto update(CategoryUpdateRequestDto request);

}

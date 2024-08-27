package com.stepanew.senlaproject.services.impl;

import com.stepanew.senlaproject.domain.dto.request.CategoryCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.CategoryUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.CategoryResponseDto;
import com.stepanew.senlaproject.domain.entity.Category;
import com.stepanew.senlaproject.domain.mapper.category.CategoryCreateRequestDtoMapper;
import com.stepanew.senlaproject.domain.mapper.category.CategoryResponseDtoMapper;
import com.stepanew.senlaproject.exceptions.CategoryException;
import com.stepanew.senlaproject.repository.CategoryRepository;
import com.stepanew.senlaproject.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryResponseDtoMapper categoryResponseDtoMapper;

    private final CategoryCreateRequestDtoMapper categoryCreateRequestDtoMapper;

    @Override
    public CategoryResponseDto findById(Long id) {
        return categoryResponseDtoMapper
                .toDto(
                        findCategoryById(id)
                );
    }

    @Override
    public Page<CategoryResponseDto> findAll(Pageable pageable, String name) {
        Page<Category> response = categoryRepository
                .findAllByNameContainingIgnoreCase(
                        name,
                        pageable
                );

        return response.map(categoryResponseDtoMapper::toDto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        categoryRepository.delete(
                findCategoryById(id)
        );
    }

    @Override
    @Transactional
    public CategoryResponseDto create(CategoryCreateRequestDto request) {
        Category category = categoryCreateRequestDtoMapper.toEntity(request);

        checkToUsingName(request.name());

        categoryRepository.save(category);

        return categoryResponseDtoMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryResponseDto update(CategoryUpdateRequestDto request) {
        Category updatedCategory = findCategoryById(request.id());

        checkToUsingName(request.name());

        if (request.name() != null) {
            updatedCategory.setName(request.name());
        }

        if (request.description() != null) {
            updatedCategory.setDescription(request.description());
        }

        categoryRepository.save(updatedCategory);

        return categoryResponseDtoMapper.toDto(updatedCategory);
    }

    private Category findCategoryById(Long id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(CategoryException.CODE.NO_SUCH_CATEGORY::get);
    }

    private void checkToUsingName(String request) {
        if (categoryRepository.findByName(request).isPresent()) {
            throw CategoryException.CODE.NAME_IN_USE.get();
        }
    }

}

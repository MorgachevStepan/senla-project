package com.stepanew.senlaproject.service;

import com.stepanew.senlaproject.domain.dto.request.CategoryCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.CategoryUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.CategoryResponseDto;
import com.stepanew.senlaproject.domain.entity.Category;
import com.stepanew.senlaproject.domain.mapper.category.CategoryCreateRequestDtoMapper;
import com.stepanew.senlaproject.domain.mapper.category.CategoryResponseDtoMapper;
import com.stepanew.senlaproject.exceptions.CategoryException;
import com.stepanew.senlaproject.repository.CategoryRepository;
import com.stepanew.senlaproject.services.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryResponseDtoMapper categoryResponseDtoMapper;

    @Mock
    private CategoryCreateRequestDtoMapper categoryCreateRequestDtoMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private final static Long DEFAULT_ID = 1L;

    private final static String DEFAULT_NAME = "NAME";

    private final static String DEFAULT_DESCRIPTION = "DESCRIPTION";

    @Test
    void findByIdTest() {
        Category category = createCategory();
        CategoryResponseDto expectedResponse = createCategoryResponseDto();

        when(categoryRepository
                .findById(DEFAULT_ID))
                .thenReturn(Optional.of(category));
        when(categoryResponseDtoMapper
                .toDto(category))
                .thenReturn(expectedResponse);

        CategoryResponseDto testCategory = categoryService.findById(DEFAULT_ID);

        verify(categoryRepository).findById(DEFAULT_ID);
        verify(categoryResponseDtoMapper).toDto(category);
        Assertions.assertEquals(expectedResponse, testCategory);
    }

    @Test
    void findByIdThrowsExceptionTest() {
        when(categoryRepository
                .findById(DEFAULT_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                CategoryException.class, () -> categoryService.findById(DEFAULT_ID)
        );
    }

    @Test
    void findAllTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Category category = createCategory();
        Page<Category> categoriesPage = new PageImpl<>(Collections.singletonList(category));
        CategoryResponseDto expectedResponse = new CategoryResponseDto(DEFAULT_ID, null, null);

        when(categoryRepository
                .findAllByNameContainingIgnoreCase("", pageable))
                .thenReturn(categoriesPage);
        when(categoryResponseDtoMapper
                .toDto(category))
                .thenReturn(expectedResponse);

        Page<CategoryResponseDto> result = categoryService.findAll(pageable, "");

        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals(expectedResponse.id(), result.getContent().get(0).id());
    }

    @Test
    void deleteTest() {
        Category category = createCategory();

        when(categoryRepository
                .findById(DEFAULT_ID))
                .thenReturn(Optional.of(category));

        categoryService.delete(DEFAULT_ID);

        verify(categoryRepository).delete(category);
    }

    @Test
    void deleteThrowsExceptionTest() {
        when(categoryRepository
                .findById(DEFAULT_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                CategoryException.class, () -> categoryService.delete(DEFAULT_ID)
        );
    }

    @Test
    void createTest() {
        CategoryCreateRequestDto request = createCategoryCreateRequestDto();
        Category category = createCategory();
        CategoryResponseDto expectedResponse = createCategoryResponseDto();

        when(categoryRepository.findByName(request.name()))
                .thenReturn(Optional.empty());
        when(categoryCreateRequestDtoMapper.toEntity(request))
                .thenReturn(category);
        when(categoryRepository.save(category))
                .thenReturn(category);
        when(categoryResponseDtoMapper.toDto(category))
                .thenReturn(expectedResponse);

        CategoryResponseDto result = categoryService.create(request);

        Assertions.assertEquals(expectedResponse, result);
    }

    @Test
    void createThrowsExceptionWhenNameInUseTest() {
        CategoryCreateRequestDto request = createCategoryCreateRequestDto();
        Category existingCategory = createCategory();

        when(categoryRepository.findByName(request.name()))
                .thenReturn(Optional.of(existingCategory));

        Assertions.assertThrows(
                CategoryException.class, () -> categoryService.create(request)
        );
    }

    @Test
    void updateTest() {
        CategoryUpdateRequestDto request = createCategoryUpdateRequestDto();
        Category category = createCategory();
        CategoryResponseDto expectedResponse = createCategoryResponseDto();

        when(categoryRepository.findById(DEFAULT_ID))
                .thenReturn(Optional.of(category));
        when(categoryRepository.findByName(request.name()))
                .thenReturn(Optional.empty());
        when(categoryRepository.save(category))
                .thenReturn(category);
        when(categoryResponseDtoMapper.toDto(category))
                .thenReturn(expectedResponse);

        CategoryResponseDto result = categoryService.update(request);

        Assertions.assertEquals(expectedResponse, result);
    }

    @Test
    void updateThrowsExceptionWhenCategoryNotFoundTest() {
        CategoryUpdateRequestDto request = createCategoryUpdateRequestDto();

        when(categoryRepository.findById(DEFAULT_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                CategoryException.class, () -> categoryService.update(request)
        );
    }

    @Test
    void updateThrowsExceptionWhenNameInUseTest() {
        CategoryUpdateRequestDto request = createCategoryUpdateRequestDto();
        Category category = createCategory();
        category.setId(DEFAULT_ID);
        Category existingCategory = new Category();
        existingCategory.setName(request.name());

        when(categoryRepository.findById(DEFAULT_ID))
                .thenReturn(Optional.of(category));
        when(categoryRepository.findByName(request.name()))
                .thenReturn(Optional.of(existingCategory));

        Assertions.assertThrows(
                CategoryException.class, () -> categoryService.update(request)
        );
    }

    private CategoryCreateRequestDto createCategoryCreateRequestDto() {
        return new CategoryCreateRequestDto(
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION
        );
    }

    private CategoryResponseDto createCategoryResponseDto() {
        return new CategoryResponseDto(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION
        );
    }

    private Category createCategory() {
        return new Category(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION,
                null
        );
    }

    private CategoryUpdateRequestDto createCategoryUpdateRequestDto() {
        return new CategoryUpdateRequestDto(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION
        );
    }

}

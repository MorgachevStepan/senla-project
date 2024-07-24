package com.stepanew.senlaproject.services;

import com.stepanew.senlaproject.domain.dto.request.ProductCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.ProductUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface ProductService {

    @Transactional(readOnly = true)
    ProductResponseDto findById(Long id);

    @Transactional(readOnly = false)
    ProductResponseDto create(ProductCreateRequestDto request);

    @Transactional(readOnly = false)
    void delete(Long id);

    @Transactional(readOnly = false)
    ProductResponseDto update(ProductUpdateRequestDto request);

    @Transactional(readOnly = true)
    Page<ProductResponseDto> findAll(Pageable pageable, String name, Long categoryId);
}

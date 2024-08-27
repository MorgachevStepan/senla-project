package com.stepanew.senlaproject.services;

import com.stepanew.senlaproject.domain.dto.request.PriceCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.ProductCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.ProductUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.PriceResponseDto;
import com.stepanew.senlaproject.domain.dto.response.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    ProductResponseDto findById(Long id);

    ProductResponseDto create(ProductCreateRequestDto request, String email);

    void delete(Long id, String email);

    ProductResponseDto update(ProductUpdateRequestDto request, String email);

    Page<ProductResponseDto> findAll(Pageable pageable, String name, Long categoryId);

    PriceResponseDto addPrice(PriceCreateRequestDto request, String email);

    void uploadProducts(MultipartFile file, String email);

    void uploadPrices(MultipartFile file, String email);

}

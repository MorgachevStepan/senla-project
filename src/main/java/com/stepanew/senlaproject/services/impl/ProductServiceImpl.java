package com.stepanew.senlaproject.services.impl;

import com.stepanew.senlaproject.domain.dto.request.ProductCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.ProductUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.ProductResponseDto;
import com.stepanew.senlaproject.domain.entity.Category;
import com.stepanew.senlaproject.domain.entity.Product;
import com.stepanew.senlaproject.domain.mapper.product.ProductCreateRequestDtoMapper;
import com.stepanew.senlaproject.domain.mapper.product.ProductResponseDtoMapper;
import com.stepanew.senlaproject.exceptions.CategoryException;
import com.stepanew.senlaproject.exceptions.ProductException;
import com.stepanew.senlaproject.repository.CategoryRepository;
import com.stepanew.senlaproject.repository.ProductRepository;
import com.stepanew.senlaproject.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final static String TEA_POT = "Чайник";

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ProductResponseDtoMapper productResponseDtoMapper;

    private final ProductCreateRequestDtoMapper productCreateRequestDtoMapper;

    @Override
    public ProductResponseDto findById(Long id) {
        return productResponseDtoMapper
                .toDto(findProductById(id));
    }

    @Override
    public ProductResponseDto create(ProductCreateRequestDto request) {
        Product product = productCreateRequestDtoMapper.toEntity(request);

        checkToUsingName(request.name());

        Category category = categoryRepository
                .findById(request.categoryId())
                .orElseThrow(CategoryException.CODE.NO_SUCH_CATEGORY::get);

        category.getProducts().add(product);
        product.setCategory(category);

        productRepository.save(product);

        return productResponseDtoMapper.toDto(product);
    }

    @Override
    public void delete(Long id) {
        Product deletedProduct = findProductById(id);

        if (deletedProduct.getName().equals(TEA_POT)) {
            throw ProductException.CODE.TEA_POT.get();
        }

        productRepository.delete(deletedProduct);
    }

    @Override
    public ProductResponseDto update(ProductUpdateRequestDto request) {
        Product updatedProduct = findProductById(request.id());

        checkToUsingName(request.name());

        if (request.name() != null) {
            updatedProduct.setName(request.name());
        }

        if (request.description() != null) {
            updatedProduct.setDescription(request.description());
        }

        productRepository.save(updatedProduct);

        return productResponseDtoMapper.toDto(updatedProduct);
    }

    @Override
    public Page<ProductResponseDto> findAll(Pageable pageable, String name, Long categoryId) {
        Page<Product> response = productRepository
                .findAllByNameContainingIgnoreCaseAndCategory_Id(
                        name,
                        categoryId,
                        pageable
                );

        return response.map(productResponseDtoMapper::toDto);
    }

    private void checkToUsingName(String request) {
        if (productRepository.findByName(request).isPresent()) {
            throw ProductException.CODE.NAME_IN_USE.get();
        }
    }

    private Product findProductById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(ProductException.CODE.NO_SUCH_PRODUCT::get);
    }

}

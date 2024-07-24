package com.stepanew.senlaproject.services.impl;

import com.stepanew.senlaproject.domain.dto.request.PriceCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.ProductCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.ProductUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.PriceResponseDto;
import com.stepanew.senlaproject.domain.dto.response.ProductResponseDto;
import com.stepanew.senlaproject.domain.entity.*;
import com.stepanew.senlaproject.domain.enums.ActionType;
import com.stepanew.senlaproject.domain.mapper.price.PriceCreateRequestDtoMapper;
import com.stepanew.senlaproject.domain.mapper.price.PriceResponseDtoMapper;
import com.stepanew.senlaproject.domain.mapper.product.ProductCreateRequestDtoMapper;
import com.stepanew.senlaproject.domain.mapper.product.ProductResponseDtoMapper;
import com.stepanew.senlaproject.exceptions.CategoryException;
import com.stepanew.senlaproject.exceptions.ProductException;
import com.stepanew.senlaproject.exceptions.StoreException;
import com.stepanew.senlaproject.exceptions.UserException;
import com.stepanew.senlaproject.repository.*;
import com.stepanew.senlaproject.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final static String TEA_POT = "Чайник";

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final UserProductsActionRepository userProductsActionRepository;

    private final StoreRepository storeRepository;

    private final PriceRepository priceRepository;

    private final ProductResponseDtoMapper productResponseDtoMapper;

    private final ProductCreateRequestDtoMapper productCreateRequestDtoMapper;

    private final PriceCreateRequestDtoMapper priceCreateRequestDtoMapper;

    private final PriceResponseDtoMapper priceResponseDtoMapper;

    @Override
    public ProductResponseDto findById(Long id) {
        return productResponseDtoMapper
                .toDto(findProductById(id));
    }

    @Override
    public ProductResponseDto create(ProductCreateRequestDto request, String email) {
        Product product = productCreateRequestDtoMapper.toEntity(request);

        checkToUsingName(request.name());

        Category category = categoryRepository
                .findById(request.categoryId())
                .orElseThrow(CategoryException.CODE.NO_SUCH_CATEGORY::get);

        category.getProducts().add(product);
        product.setCategory(category);

        logUsersAction(email, product, ActionType.ADDED);

        productRepository.save(product);

        return productResponseDtoMapper.toDto(product);
    }

    @Override
    public void delete(Long id, String email) {
        Product deletedProduct = findProductById(id);

        if (deletedProduct.getName().equals(TEA_POT)) {
            throw ProductException.CODE.TEA_POT.get();
        }

        logUsersAction(email, deletedProduct, ActionType.DELETED);

        productRepository.delete(deletedProduct);
    }

    @Override
    public ProductResponseDto update(ProductUpdateRequestDto request, String email) {
        Product updatedProduct = findProductById(request.id());

        checkToUsingName(request.name());

        if (request.name() != null) {
            updatedProduct.setName(request.name());
        }

        if (request.description() != null) {
            updatedProduct.setDescription(request.description());
        }

        productRepository.save(updatedProduct);

        logUsersAction(email, updatedProduct, ActionType.EDITED);

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

    @Override
    public PriceResponseDto addPrice(PriceCreateRequestDto request) {
        Price price = priceCreateRequestDtoMapper.toEntity(request);
        Product product = findProductById(request.productId());
        Store store = storeRepository
                .findById(request.storeId())
                .orElseThrow(StoreException.CODE.NO_SUCH_STORE::get);

        price.setCheckedDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        price.setProduct(product);
        price.setStore(store);

        store.getPrices().add(price);
        product.getPrices().add(price);

        return priceResponseDtoMapper
                .toDto(priceRepository.save(price));
    }

    private void logUsersAction(String email, Product product, ActionType actionType) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(UserException.CODE.NO_SUCH_USER::get);

        UserProductsAction userProductsAction = UserProductsAction
                .builder()
                .user(user)
                .product(product)
                .actionType(actionType)
                .actionDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        userProductsActionRepository.save(userProductsAction);
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

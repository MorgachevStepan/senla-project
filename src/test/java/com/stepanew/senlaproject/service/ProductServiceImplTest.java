package com.stepanew.senlaproject.service;

import com.stepanew.senlaproject.domain.dto.request.PriceCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.ProductCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.ProductUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.PriceResponseDto;
import com.stepanew.senlaproject.domain.dto.response.ProductResponseDto;
import com.stepanew.senlaproject.domain.entity.*;
import com.stepanew.senlaproject.domain.mapper.price.PriceCreateRequestDtoMapper;
import com.stepanew.senlaproject.domain.mapper.price.PriceResponseDtoMapper;
import com.stepanew.senlaproject.domain.mapper.product.ProductCreateRequestDtoMapper;
import com.stepanew.senlaproject.domain.mapper.product.ProductResponseDtoMapper;
import com.stepanew.senlaproject.exceptions.ProductException;
import com.stepanew.senlaproject.repository.*;
import com.stepanew.senlaproject.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProductsActionRepository userProductsActionRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private PriceRepository priceRepository;

    @Mock
    private ProductResponseDtoMapper productResponseDtoMapper;

    @Mock
    private ProductCreateRequestDtoMapper productCreateRequestDtoMapper;

    @Mock
    private PriceCreateRequestDtoMapper priceCreateRequestDtoMapper;

    @Mock
    private PriceResponseDtoMapper priceResponseDtoMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    private ProductCreateRequestDto productCreateRequestDto;

    private ProductUpdateRequestDto productUpdateRequestDto;

    private PriceCreateRequestDto priceCreateRequestDto;

    private Price price;

    private User user;

    private Category category;

    private Store store;

    private static final Long DEFAULT_ID = 1L;

    private static final String DEFAULT_NAME = "NAME";

    private static final String DEFAULT_ADDRESS = "ADDRESS";

    private static final String DEFAULT_NUMBER = "88005553535";

    private static final String DEFAULT_DESCRIPTION = "DESCRIPTION";

    private static final String DEFAULT_EMAIL = "test@example.com";

    private static final BigDecimal PRICE = BigDecimal.valueOf(100);

    @BeforeEach
    void setUp() {
        category = new Category(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION,
                new ArrayList<>()
        );
        product = new Product(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION,
                category,
                new ArrayList<>()
        );
        user = new User(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_EMAIL,
                LocalDateTime.now(),
                new HashSet<>(),
                null
        );
        store = new Store(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_NUMBER,
                DEFAULT_ADDRESS,
                new ArrayList<>()
        );
        price = new Price(
                DEFAULT_ID,
                PRICE,
                LocalDateTime.now(),
                product,
                store
        );

        productCreateRequestDto = new ProductCreateRequestDto(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION
        );
        productUpdateRequestDto = new ProductUpdateRequestDto(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION
        );
        priceCreateRequestDto = new PriceCreateRequestDto(
                DEFAULT_ID,
                DEFAULT_ID,
                PRICE
        );
    }

    @Test
    void findByIdTest() {
        when(productRepository.findById(DEFAULT_ID))
                .thenReturn(Optional.of(product));
        when(productResponseDtoMapper.toDto(product))
                .thenReturn(new ProductResponseDto(
                        DEFAULT_ID,
                        DEFAULT_NAME,
                        DEFAULT_DESCRIPTION,
                        DEFAULT_NAME
                ));

        ProductResponseDto response = productService.findById(1L);

        assertNotNull(response);
        assertEquals(DEFAULT_NAME, response.name());
        verify(productRepository).findById(DEFAULT_ID);
        verify(productResponseDtoMapper).toDto(product);
    }

    @Test
    void findByIdThrowsExceptionTest() {
        when(productRepository.findById(DEFAULT_ID))
                .thenReturn(Optional.empty());

        ProductException exception = assertThrows(ProductException.class, () -> productService.findById(DEFAULT_ID));

        assertEquals(ProductException.CODE.NO_SUCH_PRODUCT, exception.getCode());

        verify(productRepository).findById(DEFAULT_ID);
        verify(productResponseDtoMapper, never()).toDto(any(Product.class));
    }


    @Test
    void createProductTest() {
        when(categoryRepository.findById(DEFAULT_ID))
                .thenReturn(Optional.of(category));
        when(productCreateRequestDtoMapper.toEntity(productCreateRequestDto))
                .thenReturn(product);
        when(userRepository.findByEmail(DEFAULT_EMAIL))
                .thenReturn(Optional.of(user));
        when(productResponseDtoMapper.toDto(product))
                .thenReturn(new ProductResponseDto(
                        DEFAULT_ID,
                        DEFAULT_NAME,
                        DEFAULT_DESCRIPTION,
                        DEFAULT_NAME));

        ProductResponseDto response = productService.create(productCreateRequestDto, DEFAULT_EMAIL);
        assertNotNull(response);
        assertEquals(DEFAULT_NAME, response.name());
        verify(productRepository).save(product);
        verify(userProductsActionRepository).save(any(UserProductsAction.class));
    }

    @Test
    void deleteProductTest() {
        when(productRepository.findById(DEFAULT_ID))
                .thenReturn(Optional.of(product));
        when(userRepository.findByEmail(DEFAULT_EMAIL))
                .thenReturn(Optional.of(user));

        productService.delete(DEFAULT_ID, DEFAULT_EMAIL);

        verify(productRepository).delete(product);
        verify(userProductsActionRepository).save(any(UserProductsAction.class));
    }

    @Test
    void updateProductTest() {
        when(productRepository.findById(DEFAULT_ID))
                .thenReturn(Optional.of(product));
        when(productResponseDtoMapper.toDto(product))
                .thenReturn(new ProductResponseDto(
                        DEFAULT_ID,
                        DEFAULT_NAME + " new",
                        DEFAULT_DESCRIPTION + " new",
                        DEFAULT_NAME));
        when(userRepository.findByEmail(DEFAULT_EMAIL))
                .thenReturn(Optional.of(user));

        ProductResponseDto response = productService.update(productUpdateRequestDto, DEFAULT_EMAIL);

        assertNotNull(response);
        assertEquals(DEFAULT_NAME + " new", response.name());
        assertEquals(DEFAULT_DESCRIPTION + " new", response.description());
        verify(productRepository).save(product);
        verify(userProductsActionRepository).save(any(UserProductsAction.class));
    }

    @Test
    void addPriceTest() {
        when(productRepository.findById(DEFAULT_ID))
                .thenReturn(Optional.of(product));
        when(storeRepository.findById(DEFAULT_ID))
                .thenReturn(Optional.of(store));
        when(priceCreateRequestDtoMapper.toEntity(priceCreateRequestDto))
                .thenReturn(price);
        when(priceResponseDtoMapper.toDto(price))
                .thenReturn(new PriceResponseDto(
                        DEFAULT_ID, DEFAULT_ID, PRICE, LocalDateTime.now()));
        when(userRepository.findByEmail(DEFAULT_EMAIL))
                .thenReturn(Optional.of(user));
        when(priceRepository.save(price))
                .thenReturn(price);

        PriceResponseDto response = productService.addPrice(priceCreateRequestDto, DEFAULT_EMAIL);

        assertNotNull(response);
        assertEquals(PRICE, response.price());
        verify(priceRepository).save(price);
        verify(userProductsActionRepository).save(any(UserProductsAction.class));
    }

}

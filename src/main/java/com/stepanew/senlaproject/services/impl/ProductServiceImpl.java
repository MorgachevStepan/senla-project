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
import com.stepanew.senlaproject.exceptions.*;
import com.stepanew.senlaproject.repository.*;
import com.stepanew.senlaproject.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.UnsupportedFileFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@Transactional(readOnly = true)
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
    @Transactional
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
    @Transactional
    public void delete(Long id, String email) {
        Product deletedProduct = findProductById(id);

        if (deletedProduct.getName().equals(TEA_POT)) {
            throw ProductException.CODE.TEA_POT.get();
        }

        logUsersAction(email, deletedProduct, ActionType.DELETED);

        productRepository.delete(deletedProduct);
    }

    @Override
    @Transactional
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
    @Transactional
    public PriceResponseDto addPrice(PriceCreateRequestDto request, String email) {
        Price price = priceCreateRequestDtoMapper.toEntity(request);
        Product product = findProductById(request.productId());
        Store store = storeRepository
                .findById(request.storeId())
                .orElseThrow(StoreException.CODE.NO_SUCH_STORE::get);

        price.setProduct(product);
        price.setStore(store);

        store.getPrices().add(price);
        product.getPrices().add(price);

        logUsersAction(email, product, ActionType.NEW_PRICED);

        price.setCheckedDate(LocalDateTime.now());

        return priceResponseDtoMapper
                .toDto(priceRepository.save(price));
    }

    @Override
    @Transactional
    public void uploadProducts(MultipartFile file, String email) {
        List<Product> products = Collections.synchronizedList(new ArrayList<>());

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            StreamSupport.stream(sheet.spliterator(), true)
                    .skip(1)
                    .parallel()
                    .forEach(row -> {
                        Category category = categoryRepository
                                .findById((long) row.getCell(2).getNumericCellValue())
                                .orElseThrow(CategoryException.CODE.NO_SUCH_CATEGORY::get);

                        Product product = Product.builder()
                                .name(row.getCell(0).getStringCellValue())
                                .description(row.getCell(1).getStringCellValue())
                                .category(category)
                                .build();

                        products.add(product);
                    });

            List<Product> savedProducts = productRepository.saveAll(products);
            logUsersActionBatch(email, savedProducts, ActionType.ADDED);
        } catch (IllegalStateException e) {
            throw ParserException.CODE.WRONG_DATA_FORMAT.get();
        } catch (UnsupportedFileFormatException e) {
            throw ParserException.CODE.WRONG_FORMAT.get();
        } catch (IOException e) {
            throw ParserException.CODE.SOMETHING_WRONG.get();
        }
    }



    @Override
    @Transactional
    public void uploadPrices(MultipartFile file, String email) {
        List<Price> prices = Collections.synchronizedList(new ArrayList<>());
        List<Product> products = Collections.synchronizedList(new ArrayList<>());

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            StreamSupport.stream(sheet.spliterator(), true)
                    .skip(1)
                    .parallel()
                    .forEach(row -> {
                        PriceCreateRequestDto request = getPriceCreateRequestDtoFromRow(row);

                        Price price = priceCreateRequestDtoMapper.toEntity(request);
                        Product product = findProductById(request.productId());
                        Store store = storeRepository.findById(request.storeId())
                                .orElseThrow(StoreException.CODE.NO_SUCH_STORE::get);

                        price.setProduct(product);
                        price.setStore(store);
                        price.setCheckedDate(LocalDateTime.now());

                        products.add(product);
                        prices.add(price);
                    });

            logUsersActionBatch(email, products, ActionType.NEW_PRICED);
            priceRepository.saveAll(prices);
        } catch (IllegalStateException e) {
            throw ParserException.CODE.WRONG_DATA_FORMAT.get();
        } catch (UnsupportedFileFormatException e) {
            log.error(e.toString());
            throw ParserException.CODE.WRONG_FORMAT.get();
        } catch (IOException e) {
            throw ParserException.CODE.SOMETHING_WRONG.get();
        }
    }

    private static PriceCreateRequestDto getPriceCreateRequestDtoFromRow(Row row) {
        return new PriceCreateRequestDto(
                (long) row.getCell(0).getNumericCellValue(),
                (long) row.getCell(1).getNumericCellValue(),
                BigDecimal.valueOf(row.getCell(2).getNumericCellValue())
        );
    }

    private void logUsersAction(String email, Product product, ActionType actionType) {
        User user = getUserByEmail(email);

        UserProductsAction userProductsAction = UserProductsAction
                .builder()
                .email(user.getEmail())
                .product(product.getName())
                .actionType(actionType)
                .build();

        userProductsActionRepository.save(userProductsAction);
    }

    private void logUsersActionBatch(String email, List<Product> products, ActionType actionType) {
        User user = getUserByEmail(email);

        List<UserProductsAction> result = products.parallelStream()
                .map(product -> UserProductsAction.builder()
                        .email(user.getEmail())
                        .product(product.getName())
                        .actionType(actionType)
                        .build())
                .collect(Collectors.toList());

        userProductsActionRepository.saveAll(result);
    }


    private User getUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(UserException.CODE.NO_SUCH_USER_ID::get);
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

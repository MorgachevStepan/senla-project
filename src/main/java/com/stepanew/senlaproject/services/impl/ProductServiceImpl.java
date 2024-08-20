package com.stepanew.senlaproject.services.impl;

import com.stepanew.senlaproject.domain.dto.request.PriceCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.ProductCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.ProductUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.PriceBatchUploadDto;
import com.stepanew.senlaproject.domain.dto.response.PriceResponseDto;
import com.stepanew.senlaproject.domain.dto.response.ProductBatchUploadDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

@Slf4j
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

        return priceResponseDtoMapper
                .toDto(priceRepository.save(price));
    }

    @Override
    public ProductBatchUploadDto uploadProducts(MultipartFile file, String email) {
        ProductBatchUploadDto response = new ProductBatchUploadDto(new ArrayList<>());

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {

                if (row.getRowNum() == 0) {
                    continue;
                }

                Category category = categoryRepository
                        .findById((long) row.getCell(2).getNumericCellValue())
                        .orElseThrow(CategoryException.CODE.NO_SUCH_CATEGORY::get);

                Product product = Product
                        .builder()
                        .name(row.getCell(0).getStringCellValue())
                        .description(row.getCell(1).getStringCellValue())
                        .category(category)
                        .build();

                ProductCreateRequestDto request = productCreateRequestDtoMapper.toDto(product);

                response
                        .uploaded()
                        .add(create(request, email));
            }
        } catch (IllegalStateException e) {
            throw ParserException.CODE.WRONG_DATA_FORMAT.get();
        } catch (UnsupportedFileFormatException e) {
            throw ParserException.CODE.WRONG_FORMAT.get();
        } catch (IOException e) {
            throw ParserException.CODE.SOMETHING_WRONG.get();
        }

        return response;
    }

    @Override
    public PriceBatchUploadDto uploadPrices(MultipartFile file, String email) {
        PriceBatchUploadDto response = new PriceBatchUploadDto(new ArrayList<>());

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {

                if (row.getRowNum() == 0) {
                    continue;
                }

                PriceCreateRequestDto request = new PriceCreateRequestDto(
                        (long) row.getCell(0).getNumericCellValue(),
                        (long) row.getCell(1).getNumericCellValue(),
                        BigDecimal.valueOf(row.getCell(2).getNumericCellValue())
                );

                response
                        .uploaded()
                        .add(addPrice(request, email));
            }
        } catch (IllegalStateException e) {
            throw ParserException.CODE.WRONG_DATA_FORMAT.get();
        } catch (UnsupportedFileFormatException e) {
            log.error(e.toString());
            throw ParserException.CODE.WRONG_FORMAT.get();
        } catch (IOException e) {
            throw ParserException.CODE.SOMETHING_WRONG.get();
        }

        return response;
    }

    private void logUsersAction(String email, Product product, ActionType actionType) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(UserException.CODE.NO_SUCH_USER_ID::get);

        UserProductsAction userProductsAction = UserProductsAction
                .builder()
                .email(user.getEmail())
                .product(product.getName())
                .actionType(actionType)
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

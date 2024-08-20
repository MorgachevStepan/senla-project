package com.stepanew.senlaproject.controller;

import com.stepanew.senlaproject.domain.dto.request.PriceCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.ProductCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.ProductUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.PriceBatchUploadDto;
import com.stepanew.senlaproject.domain.dto.response.PriceResponseDto;
import com.stepanew.senlaproject.domain.dto.response.ProductBatchUploadDto;
import com.stepanew.senlaproject.domain.dto.response.ProductResponseDto;
import com.stepanew.senlaproject.exceptions.ParserException;
import com.stepanew.senlaproject.exceptions.ProductException;
import com.stepanew.senlaproject.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest extends AbstractControllerTest {

    @MockBean
    private ProductService productService;

    private static final String PATH = "/api/v1/product";

    private ProductResponseDto responseDto;

    private ProductCreateRequestDto createRequest;

    private ProductUpdateRequestDto updateRequest;

    private PriceResponseDto priceResponseDto;

    private PriceCreateRequestDto priceCreateRequestDto;

    private MockMultipartFile file;

    @BeforeEach
    void setUp() {
        responseDto = new ProductResponseDto(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION,
                DEFAULT_NAME
        );
        createRequest = new ProductCreateRequestDto(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION
        );
        updateRequest = new ProductUpdateRequestDto(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION
        );
        priceResponseDto = new PriceResponseDto(
                DEFAULT_ID,
                DEFAULT_ID,
                DEFAULT_PRICE,
                LocalDateTime.now()
        );
        priceCreateRequestDto = new PriceCreateRequestDto(
                DEFAULT_ID,
                DEFAULT_ID,
                DEFAULT_PRICE
        );
        file = new MockMultipartFile(
                "file",
                "products.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "test data".getBytes()
        );
    }

    //The beginning of getProductById method tests
    //Test 200 OK
    @Test
    @WithMockUser
    void getProductByIdTest() throws Exception {
        when(productService.findById(anyLong())).thenReturn(responseDto);

        mockMvc.perform(get(PATH + "/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    //Test 404 Not Found
    @Test
    @WithMockUser
    void getProductByIdNotFoundTest() throws Exception {
        when(productService.findById(anyLong()))
                .thenThrow(ProductException.CODE.NO_SUCH_PRODUCT.get());

        mockMvc.perform(get(PATH + "/{id}", DEFAULT_ID))
                .andExpect(status().isNotFound());
    }

    //Test 400 Bad Request
    @Test
    @WithMockUser
    void getProductByIdBadRequestTest() throws Exception {
        mockMvc.perform(get(PATH + "/{id}", DEFAULT_NAME))
                .andExpect(status().isBadRequest());
    }

    //Test 401 Unauthorized
    @Test
    @WithAnonymousUser
    void getProductByIdUnauthorizedTest() throws Exception {
        mockMvc.perform(get(PATH + "/{id}", DEFAULT_ID))
                .andExpect(status().isUnauthorized());
    }
    //The end of getProductById method tests

    //The beginning of getAllByCategory method tests
    //Test 200 OK
    @Test
    @WithMockUser
    void findAllTest() throws Exception {
        Page<ProductResponseDto> page = new PageImpl<>(Collections.singletonList(responseDto));
        when(productService.findAll(any(PageRequest.class), any(String.class), anyLong())).thenReturn(page);

        mockMvc.perform(get(PATH + "/")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("categoryId", Long.toString(DEFAULT_ID))
                        .param("sortBy", "id")
                        .param("sortDirection", "asc"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));
    }

    //Test 204 No Content
    @Test
    @WithMockUser
    void findAllNoContentTest() throws Exception {
        when(productService.findAll(any(PageRequest.class), any(String.class), anyLong())).thenReturn(Page.empty());

        mockMvc.perform(get(PATH + "/")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("categoryId", Long.toString(DEFAULT_ID))
                        .param("sortBy", "id")
                        .param("sortDirection", "asc"))
                .andExpect(status().isNoContent());
    }

    //Test 400 Bad Request
    @Test
    @WithMockUser
    void findAllBadRequestTest() throws Exception {
        mockMvc.perform(get(PATH + "/")
                        .param("pageNumber", "-1")
                        .param("pageSize", "10")
                        .param("categoryId", Long.toString(DEFAULT_ID))
                        .param("sortBy", "id")
                        .param("sortDirection", "asc"))
                .andExpect(status().isBadRequest());
    }

    // Test 401 Unauthorized for searchProducts method
    @Test
    @WithAnonymousUser
    void findAllUnauthorizedTest() throws Exception {
        mockMvc.perform(get(PATH + "/")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("categoryId", Long.toString(DEFAULT_ID))
                        .param("sortBy", "id")
                        .param("sortDirection", "asc"))
                .andExpect(status().isUnauthorized());
    }
    //The end of getAllByCategory method tests

    //the beginning of create method tests
    //test code 201 created
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void createTest() throws Exception {
        when(productService.create(any(ProductCreateRequestDto.class), any(String.class))).thenReturn(responseDto);

        mockMvc.perform(post(PATH + "/")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated());
    }

    //test code 400
    @Test
    @WithMockUser(roles = "ADMIN")
    void createBadRequestTest() throws Exception {
        ProductCreateRequestDto createRequest = new ProductCreateRequestDto(
                -1L,
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION
        );

        mockMvc.perform(post(PATH + "/")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
    }

    //test code 401
    @Test
    @WithAnonymousUser
    void createUnauthorizedTest() throws Exception {
        mockMvc.perform(post(PATH + "/")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isUnauthorized());
    }

    //test code 403
    @Test
    @WithMockUser(roles = "USER")
    void createForbiddenTest() throws Exception {
        mockMvc.perform(post(PATH + "/")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isForbidden());
    }

    //the end of create method tests

    //the beginnig of delete method tests
    // Test 204 No Content
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTest() throws Exception {
        doNothing().when(productService).delete(anyLong(), any(String.class));

        mockMvc.perform(delete(PATH + "/{id}", DEFAULT_ID)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    // Test 400 Bad Request
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteBadRequestTest() throws Exception {
        mockMvc.perform(delete(PATH + "/{id}",  DEFAULT_NAME)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    // Test 401 Unauthorized
    @Test
    @WithAnonymousUser
    void deleteUnauthorizedTest() throws Exception {
        mockMvc.perform(delete(PATH + "/{id}", DEFAULT_ID)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    // Test 403 Forbidden
    @Test
    @WithMockUser(roles = "USER")
    void deleteForbiddenTest() throws Exception {
        mockMvc.perform(delete(PATH + "/{id}", DEFAULT_ID))
                .andExpect(status().isForbidden());
    }

    // Test 404 Not Found
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteNotFoundTest() throws Exception {
        doThrow(ProductException.CODE.NO_SUCH_PRODUCT.get()).
                when(productService).delete(anyLong(), any(String.class));

        mockMvc.perform(delete(PATH + "/{id}", DEFAULT_ID)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    // Test 418 Tea Pot
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteIAmTeaPotTest() throws Exception {
        doThrow(ProductException.CODE.TEA_POT.get()).
                when(productService).delete(anyLong(), any(String.class));

        mockMvc.perform(delete(PATH + "/{id}", DEFAULT_ID)
                        .with(csrf()))
                .andExpect(status().isIAmATeapot());
    }
    //the end of delete method tests

    //the beginnig of update method tests
    // Test 200 OK
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateTest() throws Exception {
        when(productService.update(any(ProductUpdateRequestDto.class), any(String.class))).thenReturn(responseDto);

        mockMvc.perform(put(PATH + "/")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    // Test 400 Bad Request
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateBadRequestTest() throws Exception {
        ProductUpdateRequestDto invalidUpdateRequest = new ProductUpdateRequestDto(
                null,
                null,
                null
        );
        mockMvc.perform(put(PATH + "/")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(invalidUpdateRequest)))
                .andExpect(status().isBadRequest());
    }

    // Test 401 Unauthorized
    @Test
    @WithAnonymousUser
    void updateUnauthorizedTest() throws Exception {
        mockMvc.perform(put(PATH + "/")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isUnauthorized());
    }

    // Test 403 Forbidden
    @Test
    @WithMockUser(roles = "USER")
    void updateForbiddenTest() throws Exception {
        mockMvc.perform(put(PATH + "/")
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isForbidden());
    }

    // Test 404 Not Found
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateNotFoundTest() throws Exception {
        when(productService.update(any(ProductUpdateRequestDto.class), any(String.class)))
                .thenThrow(ProductException.CODE.NO_SUCH_PRODUCT.get());

        mockMvc.perform(put(PATH + "/")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    // Test 409 Conflict
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateStoreNotFoundTest() throws Exception {
        when(productService.update(any(ProductUpdateRequestDto.class), any(String.class)))
                .thenThrow(ProductException.CODE.NAME_IN_USE.get());

        mockMvc.perform(put(PATH + "/")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isConflict());
    }
    //the end of update method tests

    //the beginnig of addPriceToProduct method tests
    // Test 200 Ok
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void addPriceToProductTest() throws Exception {
        when(productService.addPrice(any(PriceCreateRequestDto.class), any(String.class)))
                .thenReturn(priceResponseDto);

        mockMvc.perform(post(PATH + "/price")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(priceCreateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(priceResponseDto)));
    }

    // Test 400 Bad Request
    @Test
    @WithMockUser(roles = "ADMIN")
    void addPriceToProductBadRequestTest() throws Exception {
        PriceCreateRequestDto invalidCreateRequest = new PriceCreateRequestDto(
                null,
                null,
                null);

        mockMvc.perform(post(PATH + "/price")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(invalidCreateRequest)))
                .andExpect(status().isBadRequest());
    }

    // Test 401 Unauthorized
    @Test
    @WithAnonymousUser
    void addPriceToProductUnauthorizedTest() throws Exception {
        mockMvc.perform(post(PATH + "/price")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(priceCreateRequestDto)))
                .andExpect(status().isUnauthorized());
    }

    // Test 403 Forbidden
    @Test
    @WithMockUser(roles = "USER")
    void addPriceToProductForbiddenTest() throws Exception {
        mockMvc.perform(post(PATH + "/price")
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(priceCreateRequestDto)))
                .andExpect(status().isForbidden());
    }

    // Test 404 Not Found
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void addPriceToProductNotFoundTest() throws Exception {
        when(productService.addPrice(any(PriceCreateRequestDto.class), any(String.class)))
                .thenThrow(ProductException.CODE.NO_SUCH_PRODUCT.get());

        mockMvc.perform(post(PATH + "/price")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(priceCreateRequestDto)))
                .andExpect(status().isNotFound());
    }
    //the end of addPriceToProduct method tests

    //the beginning of uploadProducts method tests
    // Test 200 OK
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void uploadProductsTest() throws Exception {
        when(productService.uploadProducts(any(MultipartFile.class), any(String.class)))
                .thenReturn(new ProductBatchUploadDto(new ArrayList<>(List.of(responseDto))));

        mockMvc.perform(multipart(PATH + "/batch")
                        .file(file)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new ProductBatchUploadDto(new ArrayList<>(List.of(responseDto)))))
                );
    }

    // Test 400 Bad Request
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void uploadProductsBadRequestTest() throws Exception {
        when(productService.uploadProducts(any(MultipartFile.class), any(String.class)))
                .thenThrow(ParserException.CODE.WRONG_DATA_FORMAT.get());
        mockMvc.perform(multipart(PATH + "/batch")
                        .file(file)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    // Test 401 Unauthorized
    @Test
    @WithAnonymousUser
    void uploadProductsUnauthorizedTest() throws Exception {
        mockMvc.perform(multipart(PATH + "/batch")
                        .file(file)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isUnauthorized());
    }

    // Test 403 Forbidden
    @Test
    @WithMockUser(roles = "USER")
    void uploadProductsForbiddenTest() throws Exception {
        mockMvc.perform(multipart(PATH + "/batch")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isForbidden());
    }

    // Test 404 Not Found
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void uploadProductsNotFoundTest() throws Exception {
        when(productService.uploadProducts(any(MultipartFile.class), any(String.class)))
                .thenThrow(ProductException.CODE.NO_SUCH_PRODUCT.get());

        mockMvc.perform(multipart(PATH + "/batch")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isForbidden());
    }

    // Test 500 Internal Server Error
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void uploadProductsServerErrorTest() throws Exception {
        when(productService.uploadProducts(any(MultipartFile.class), any(String.class)))
                .thenThrow(ParserException.CODE.SOMETHING_WRONG.get());

        mockMvc.perform(multipart(PATH + "/batch")
                        .file(file)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError());
    }
    //the end of uploadProducts method tests

    //the beginning of uploadPrices method tests
    // Test 200 OK
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void uploadPricesTest() throws Exception {
        when(productService.uploadPrices(any(MultipartFile.class), any(String.class)))
                .thenReturn(new PriceBatchUploadDto(new ArrayList<>(List.of(priceResponseDto))));

        mockMvc.perform(multipart(PATH + "/batch/price")
                        .file(file)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new PriceBatchUploadDto(new ArrayList<>(List.of(priceResponseDto)))))
                );
    }

    // Test 400 Bad Request
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void uploadPricesBadRequestTest() throws Exception {
        when(productService.uploadPrices(any(MultipartFile.class), any(String.class)))
                .thenThrow(ParserException.CODE.WRONG_DATA_FORMAT.get());
        mockMvc.perform(multipart(PATH + "/batch/price")
                        .file(file)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    // Test 401 Unauthorized
    @Test
    @WithAnonymousUser
    void uploadPricesUnauthorizedTest() throws Exception {
        mockMvc.perform(multipart(PATH + "/batch/price")
                        .file(file)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isUnauthorized());
    }

    // Test 403 Forbidden
    @Test
    @WithMockUser(roles = "USER")
    void uploadPricesForbiddenTest() throws Exception {
        mockMvc.perform(multipart(PATH + "/batch/price")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isForbidden());
    }

    // Test 404 Not Found
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void uploadPricesNotFoundTest() throws Exception {
        when(productService.uploadPrices(any(MultipartFile.class), any(String.class)))
                .thenThrow(ProductException.CODE.NO_SUCH_PRODUCT.get());

        mockMvc.perform(multipart(PATH + "/batch/price")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isForbidden());
    }

    // Test 500 Internal Server Error
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void uploadPricesServerErrorTest() throws Exception {
        when(productService.uploadPrices(any(MultipartFile.class), any(String.class)))
                .thenThrow(ParserException.CODE.SOMETHING_WRONG.get());

        mockMvc.perform(multipart(PATH + "/batch/price")
                        .file(file)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError());
    }
    //the end of uploadPrices method tests

}

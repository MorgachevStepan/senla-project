package com.stepanew.senlaproject.controller;

import com.stepanew.senlaproject.domain.dto.request.StoreCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.StoreUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.StoreResponseDto;
import com.stepanew.senlaproject.exceptions.StoreException;
import com.stepanew.senlaproject.services.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoreController.class)
public class StoreControllerTest extends AbstractControllerTest{

    @MockBean
    private StoreService storeService;

    private static final String PATH = "/api/v1/store";

    private static final String DEFAULT_ADDRESS = "Store Address";

    private static final String DEFAULT_NUMBER = "88005553535";

    private StoreResponseDto responseDto;

    private StoreCreateRequestDto createRequest;

    private StoreUpdateRequestDto updateRequest;

    @BeforeEach
    void setUp() {
        responseDto = new StoreResponseDto(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_NUMBER,
                DEFAULT_ADDRESS
        );
        createRequest = new StoreCreateRequestDto(
                DEFAULT_NAME,
                DEFAULT_NUMBER,
                DEFAULT_ADDRESS
        );
        updateRequest = new StoreUpdateRequestDto(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_NUMBER,
                DEFAULT_ADDRESS
        );
    }

    //the beginnig of getById method tests
    // Test 200 OK
    @Test
    @WithMockUser
    void getByIdTest() throws Exception {
        when(storeService.findById(anyLong())).thenReturn(responseDto);

        mockMvc.perform(get(PATH + "/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    // Test 400 Bad Request
    @Test
    @WithMockUser
    void getByIdBadRequestTest() throws Exception {
        mockMvc.perform(get(PATH + "/{id}", DEFAULT_NAME))
                .andExpect(status().isBadRequest());
    }

    // Test 401 Unauthorized
    @Test
    void getByIdUnauthorizedTest() throws Exception {
        mockMvc.perform(get(PATH + "/{id}", DEFAULT_ID))
                .andExpect(status().isUnauthorized());
    }

    // Test 404 Not Found
    @Test
    @WithMockUser
    void getByIdNotFoundTest() throws Exception {
        when(storeService.findById(anyLong())).thenThrow(StoreException.CODE.NO_SUCH_STORE.get());

        mockMvc.perform(get(PATH + "/{id}", DEFAULT_ID))
                .andExpect(status().isNotFound());
    }
    //the end of getById method tests

    //the beginnig of getAll method tests
    // Test 200 OK
    @Test
    @WithMockUser
    void getAllTest() throws Exception {
        Page<StoreResponseDto> page = new PageImpl<>(Collections.singletonList(responseDto));
        when(storeService.findAll(any(PageRequest.class), any(String.class), any(String.class))).thenReturn(page);

        mockMvc.perform(get(PATH + "/")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "id")
                        .param("sortDirection", "asc"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));
    }

    // Test 204 No Content
    @Test
    @WithMockUser
    void getAllNoContentTest() throws Exception {
        when(storeService.findAll(any(PageRequest.class), any(String.class), any(String.class))).thenReturn(Page.empty());

        mockMvc.perform(get(PATH + "/")
                        .param("pageNumber", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isNoContent());
    }

    // Test 400 Bad Request
    @Test
    @WithMockUser
    void getAllBadRequestTest() throws Exception {
        mockMvc.perform(get(PATH + "/")
                        .param("pageNumber", "-1"))
                .andExpect(status().isBadRequest());
    }

    // Test 401 Unauthorized
    @Test
    void getAllUnauthorizedTest() throws Exception {
        mockMvc.perform(get(PATH + "/"))
                .andExpect(status().isUnauthorized());
    }
    //the end of getAll method tests

    //the beginnig of create method tests
    // Test 201 Created
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void createStoreTest() throws Exception {
        when(storeService.create(any(StoreCreateRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post(PATH + "/")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated());
    }

    // Test 400 Bad Request
    @Test
    @WithMockUser(roles = "ADMIN")
    void createStoreBadRequestTest() throws Exception {
        StoreCreateRequestDto invalidCreateRequest = new StoreCreateRequestDto(
                null,
                null,
                null);
        when(storeService.create(any(StoreCreateRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post(PATH + "/")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(invalidCreateRequest)))
                .andExpect(status().isBadRequest());
    }

    // Test 401 Unauthorized
    @Test
    void createStoreUnauthorizedTest() throws Exception {
        mockMvc.perform(post(PATH + "/")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isUnauthorized());
    }

    // Test 403 Forbidden
    @Test
    @WithMockUser(roles = "USER")
    void createStoreForbiddenTest() throws Exception {
        mockMvc.perform(post(PATH + "/")
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isForbidden());
    }
    //the end of create method tests

    //the beginnig of delete method tests
    // Test 204 No Content
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteStoreTest() throws Exception {
        doNothing().when(storeService).delete(anyLong());

        mockMvc.perform(delete(PATH + "/{id}", DEFAULT_ID)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    // Test 400 Bad Request
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteStoreBadRequestTest() throws Exception {
        mockMvc.perform(delete(PATH + "/{id}", "invalid")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    // Test 401 Unauthorized
    @Test
    void deleteStoreUnauthorizedTest() throws Exception {
        mockMvc.perform(delete(PATH + "/{id}", DEFAULT_ID)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    // Test 403 Forbidden
    @Test
    @WithMockUser(roles = "USER")
    void deleteStoreForbiddenTest() throws Exception {
        mockMvc.perform(delete(PATH + "/{id}", DEFAULT_ID))
                .andExpect(status().isForbidden());
    }

    // Test 404 Not Found
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteStoreNotFoundTest() throws Exception {
        doThrow(StoreException.CODE.NO_SUCH_STORE.get()).when(storeService).delete(anyLong());

        mockMvc.perform(delete(PATH + "/{id}", DEFAULT_ID)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
    //the end of delete method tests

    //the beginnig of update method tests
    // Test 200 OK
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateStoreTest() throws Exception {
        when(storeService.update(any(StoreUpdateRequestDto.class))).thenReturn(responseDto);

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
    void updateStoreBadRequestTest() throws Exception {
        StoreUpdateRequestDto invalidUpdateRequest = new StoreUpdateRequestDto(
                null,
                null,
                null,
                null);

        mockMvc.perform(put(PATH + "/")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(invalidUpdateRequest)))
                .andExpect(status().isBadRequest());
    }

    // Test 401 Unauthorized
    @Test
    void updateStoreUnauthorizedTest() throws Exception {
        mockMvc.perform(put(PATH + "/")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isUnauthorized());
    }

    // Test 403 Forbidden
    @Test
    @WithMockUser(roles = "USER")
    void updateStoreForbiddenTest() throws Exception {
        mockMvc.perform(put(PATH + "/")
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isForbidden());
    }

    // Test 404 Not Found
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateStoreNotFoundTest() throws Exception {
        when(storeService.update(any(StoreUpdateRequestDto.class))).thenThrow(StoreException.CODE.NO_SUCH_STORE.get());

        mockMvc.perform(put(PATH + "/")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }
    //the end of update method tests

}

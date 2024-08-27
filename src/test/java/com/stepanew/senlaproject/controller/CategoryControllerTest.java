package com.stepanew.senlaproject.controller;

import com.stepanew.senlaproject.domain.dto.request.CategoryCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.CategoryUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.CategoryResponseDto;
import com.stepanew.senlaproject.exceptions.CategoryException;
import com.stepanew.senlaproject.services.CategoryService;
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

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest extends AbstractControllerTest{

    @MockBean
    private CategoryService categoryService;

    private final static String PATH = "/api/v1/category";

    private CategoryResponseDto responseDto;

    private CategoryCreateRequestDto createRequest;

    private CategoryUpdateRequestDto updateRequest;


    @BeforeEach
    void setUp() {
        responseDto = new CategoryResponseDto(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION
        );
        createRequest = new CategoryCreateRequestDto(
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION
        );
        updateRequest = new CategoryUpdateRequestDto(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION
        );
    }

    //the beginning of findById method tests
    //test code 200
    @Test
    @WithMockUser
    void getByIdTest() throws Exception {
        when(categoryService.findById(DEFAULT_ID)).thenReturn(responseDto);
        mockMvc.perform(get(PATH + "/{id}", DEFAULT_ID).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    //test code 400
    @Test
    @WithMockUser
    void getByIdBadRequestTest() throws Exception {
        mockMvc.perform(get(PATH + "/{id}", "invalid"))
                .andExpect(status().isBadRequest());
    }

    //test code 401
    @Test
    void getByIdUnauthorizedTest() throws Exception {
        when(categoryService.findById(anyLong())).thenReturn(responseDto);
        mockMvc.perform(get(PATH + "/{id}", DEFAULT_ID))
                .andExpect(status().isUnauthorized());
    }


    //test code 404
    @Test
    @WithMockUser
    void getByIdNotFoundTest() throws Exception {
        when(categoryService.findById(anyLong())).thenThrow(CategoryException.CODE.NO_SUCH_CATEGORY.get());

        mockMvc.perform(get(PATH + "/{id}", DEFAULT_ID))
                .andExpect(status().isNotFound());
    }
    //the end of findById method tests

    //the beginning of findAll method tests
    //test code 200
    @Test
    @WithMockUser
    void getAllTest() throws Exception {
        Page<CategoryResponseDto> page = new PageImpl<>(Collections.singletonList(responseDto));
        when(categoryService.findAll(any(PageRequest.class), any(String.class))).thenReturn(page);

        mockMvc.perform(get(PATH + "/")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "id")
                        .param("sortDirection", "ASC"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));
    }

    //test code 400
    @Test
    @WithMockUser
    void getAllBadRequestTest() throws Exception {
        mockMvc.perform(get(PATH + "/")
                        .param("pageNumber", "-1"))
                .andExpect(status().isBadRequest());
    }

    //test code 401
    @Test
    void getAllUnauthorizedTest() throws Exception {
        mockMvc.perform(get(PATH + "/"))
                .andExpect(status().isUnauthorized());
    }
    //the end of findByAll method tests

    //the beginning of create method tests
    //test code 201
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void createTest() throws Exception {
        when(categoryService.create(any(CategoryCreateRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post(PATH + "/")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(responseDto)))
                .andExpect(status().isCreated());
    }

    //test code 400
    @Test
    @WithMockUser(roles = "ADMIN")
    void createBadRequestTest() throws Exception {
        CategoryCreateRequestDto createRequest = new CategoryCreateRequestDto(null, "New Description");
        when(categoryService.create(any(CategoryCreateRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post(PATH + "/")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
    }

    //test code 401
    @Test
    void createUnauthorizedTest() throws Exception {
        mockMvc.perform(post(PATH + "/")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isUnauthorized());
    }

    //test code 403
    @Test
    @WithMockUser
    void createForbiddenTest() throws Exception {
        mockMvc.perform(post(PATH + "/")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isForbidden());
    }

    //test code 409
    @Test
    @WithMockUser(roles = "ADMIN")
    void createConflictTest() throws Exception {
        CategoryCreateRequestDto createRequest = new CategoryCreateRequestDto("Existing Category", "Description");
        when(categoryService.create(any())).thenThrow(CategoryException.CODE.NAME_IN_USE.get());

        mockMvc.perform(post(PATH + "/")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isConflict());
    }
    //the end of create method tests

    //the beginning of delete method tests
    //test code 204
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTest() throws Exception {
        doNothing().when(categoryService).delete(anyLong());

        mockMvc.perform(delete(PATH + "/{id}", DEFAULT_ID).with(csrf()))
                .andExpect(status().isNoContent());
    }

    //test code 400
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteBadRequestTest() throws Exception {
        mockMvc.perform(delete(PATH + "/{id}", DEFAULT_NAME)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    //test code 401
    @Test
    void deleteUnauthorized() throws Exception {
        mockMvc.perform(delete(PATH + "/{id}", DEFAULT_ID).with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    //test code 403
    @Test
    @WithMockUser(roles = "USER")
    void deleteForbiddenTest() throws Exception {
        mockMvc.perform(delete(PATH + "/{id}", DEFAULT_ID))
                .andExpect(status().isForbidden());
    }

    //test code 404
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteNotFoundTest() throws Exception {
        doThrow(CategoryException.CODE.NO_SUCH_CATEGORY.get()).when(categoryService).delete(anyLong());

        mockMvc.perform(delete(PATH + "/{id}", DEFAULT_ID).with(csrf()))
                .andExpect(status().isNotFound());
    }
    //the end of delete method tests

    //the beginning of update method tests
    //test code 200
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateTest() throws Exception {
        when(categoryService.update(any(CategoryUpdateRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put(PATH + "/")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    //test code 400
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateBadRequestTest() throws Exception {
        CategoryUpdateRequestDto updateRequest = new CategoryUpdateRequestDto(null, null, null);

        mockMvc.perform(put(PATH + "/")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    //test code 401
    @Test
    void updateUnauthorizedTest() throws Exception {
        mockMvc.perform(put(PATH + "/")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isUnauthorized());
    }

    //test code 403
    @Test
    @WithMockUser(roles = "USER")
    void updateForbiddenTest() throws Exception {
        mockMvc.perform(put(PATH + "/")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isForbidden());
    }

    //test code 404
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateNotFoundTest() throws Exception {
        when(categoryService.update(any(CategoryUpdateRequestDto.class)))
                .thenThrow(CategoryException.CODE.NO_SUCH_CATEGORY.get());

        mockMvc.perform(put(PATH + "/")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    //test code 409
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateConflict() throws Exception {
        when(categoryService.update(any())).thenThrow(CategoryException.CODE.NAME_IN_USE.get());

        mockMvc.perform(put(PATH + "/")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isConflict());
    }
    //the end of update method tests


}

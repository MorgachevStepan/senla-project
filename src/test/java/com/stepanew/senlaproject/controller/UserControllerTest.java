package com.stepanew.senlaproject.controller;

import com.stepanew.senlaproject.domain.dto.request.UserAddRoleRequestDto;
import com.stepanew.senlaproject.domain.dto.request.UserUpdateMeRequestDto;
import com.stepanew.senlaproject.domain.dto.response.UserAddRoleResponseDto;
import com.stepanew.senlaproject.domain.dto.response.UserUpdateMeResponseDto;
import com.stepanew.senlaproject.exceptions.UserException;
import com.stepanew.senlaproject.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest extends AbstractControllerTest {

    @MockBean
    private UserService userService;

    private static final String PATH = "/api/v1/user";

    private static final String LONG_STRING = "1".repeat(256);


    private UserUpdateMeRequestDto updateMeRequest;

    private UserUpdateMeResponseDto updateMeResponse;

    private UserAddRoleRequestDto addRoleRequest;

    private UserAddRoleResponseDto addRoleResponse;

    @BeforeEach
    void setUp() {
        updateMeRequest = new UserUpdateMeRequestDto(
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_PATRONYMIC
        );
        updateMeResponse = new UserUpdateMeResponseDto(
                DEFAULT_EMAIL,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_PATRONYMIC
        );
        addRoleRequest = new UserAddRoleRequestDto(DEFAULT_EMAIL);
        addRoleResponse = new UserAddRoleResponseDto(DEFAULT_EMAIL);
    }

    // The beginning of updateMe method tests
    // Test 200 OK
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void updateMeTest() throws Exception {
        when(userService.updateMe(any(UserUpdateMeRequestDto.class), any(String.class)))
                .thenReturn(updateMeResponse);

        mockMvc.perform(put(PATH + "/")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(updateMeRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updateMeResponse)));
    }

    // Test 400 Bad Request
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void updateMeBadRequestTest() throws Exception {
        UserUpdateMeRequestDto invalidRequest = new UserUpdateMeRequestDto(
                LONG_STRING,
                null,
                null
        );

        mockMvc.perform(put(PATH + "/")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    // Test 401 Unauthorized
    @Test
    void updateMeUnauthorizedTest() throws Exception {
        mockMvc.perform(put(PATH + "/")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(updateMeRequest)))
                .andExpect(status().isUnauthorized());
    }

    // Test 404 Not Found
    @Test
    @WithMockUser
    void updateMeNotFoundTest() throws Exception {
        when(userService.updateMe(any(UserUpdateMeRequestDto.class), any(String.class)))
                .thenThrow(UserException.CODE.NO_SUCH_USER_EMAIL.get());

        mockMvc.perform(put(PATH + "/")
                        .with(csrf()).contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(updateMeRequest)))
                .andExpect(status().isNotFound());
    }
    // The end of updateMe method tests

    // The beginning of updateById method tests
    // Test 200 OK
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateByIdTest() throws Exception {
        when(userService.updateById(any(UserUpdateMeRequestDto.class), anyLong())).thenReturn(updateMeResponse);

        mockMvc.perform(put(PATH + "/{id}", DEFAULT_ID)
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(updateMeRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updateMeResponse)));
    }

    // Test 400 Bad Request
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateByIdBadRequestTest() throws Exception {
        UserUpdateMeRequestDto invalidRequest = new UserUpdateMeRequestDto(
                LONG_STRING,
                null,
                null
        );

        mockMvc.perform(put(PATH + "/{id}", DEFAULT_ID)
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    // Test 401 Unauthorized
    @Test
    void updateByIdUnauthorizedTest() throws Exception {
        mockMvc.perform(put(PATH + "/{id}", DEFAULT_ID)
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(updateMeRequest)))
                .andExpect(status().isUnauthorized());
    }

    // Test 403 Forbidden
    @Test
    @WithMockUser(roles = "USER")
    void updateByIdForbiddenTest() throws Exception {
        mockMvc.perform(put(PATH + "/{id}", DEFAULT_ID)
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(updateMeRequest)))
                .andExpect(status().isForbidden());
    }

    // Test 404 Not Found
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateByIdNotFoundTest() throws Exception {
        when(userService.updateById(any(UserUpdateMeRequestDto.class), anyLong()))
                .thenThrow(UserException.CODE.NO_SUCH_USER_ID.get());

        mockMvc.perform(put(PATH + "/{id}", DEFAULT_ID)
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(updateMeRequest)))
                .andExpect(status().isNotFound());
    }
    // The end of updateById method tests

    // The beginning of addAdminRole method tests
    // Test 200 OK
    @Test
    @WithMockUser(roles = "ADMIN")
    void addAdminRoleTest() throws Exception {
        when(userService.addAdminRole(any(UserAddRoleRequestDto.class)))
                .thenReturn(addRoleResponse);

        mockMvc.perform(put(PATH + "/admin")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(addRoleRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(addRoleResponse)));
    }

    // Test 400 Bad Request
    @Test
    @WithMockUser(roles = "ADMIN")
    void addAdminRoleBadRequestTest() throws Exception {
        UserAddRoleRequestDto invalidRequest = new UserAddRoleRequestDto(null);

        mockMvc.perform(put(PATH + "/admin")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    // Test 401 Unauthorized
    @Test
    @WithAnonymousUser
    void addAdminRoleUnauthorizedTest() throws Exception {
        mockMvc.perform(put(PATH + "/admin")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(addRoleRequest)))
                .andExpect(status().isUnauthorized());
    }

    // Test 403 Forbidden
    @Test
    @WithMockUser(roles = "USER")
    void addAdminRoleForbiddenTest() throws Exception {
        mockMvc.perform(put(PATH + "/admin")
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(addRoleRequest)))
                .andExpect(status().isForbidden());
    }

    // Test 404 Not Found
    @Test
    @WithMockUser(roles = "ADMIN")
    void addAdminRoleNotFoundTest() throws Exception {
        when(userService.addAdminRole(any(UserAddRoleRequestDto.class)))
                .thenThrow(UserException.CODE.NO_SUCH_USER_EMAIL.get());

        mockMvc.perform(put(PATH + "/admin")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(addRoleRequest)))
                .andExpect(status().isNotFound());
    }

    // Test 409 Conflict
    @Test
    @WithMockUser(roles = "ADMIN")
    void addAdminRoleConflictTest() throws Exception {
        when(userService.addAdminRole(any(UserAddRoleRequestDto.class)))
                .thenThrow(UserException.CODE.USER_IS_ALREADY_ADMIN.get());

        mockMvc.perform(put(PATH + "/admin")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(addRoleRequest)))
                .andExpect(status().isConflict());
    }
    // The end of addAdminRole method tests

}

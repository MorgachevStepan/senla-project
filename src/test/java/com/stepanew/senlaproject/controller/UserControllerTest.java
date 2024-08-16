package com.stepanew.senlaproject.controller;

import com.stepanew.senlaproject.domain.dto.request.UserUpdateMeRequestDto;
import com.stepanew.senlaproject.domain.dto.response.UserUpdateMeResponseDto;
import com.stepanew.senlaproject.exceptions.AuthException;
import com.stepanew.senlaproject.exceptions.UserException;
import com.stepanew.senlaproject.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    private static final Long DEFAULT_ID = 1L;

    private static final String PATH = "/api/v1/user";

    private static final String CONTENT_TYPE = "application/json";

    private static final String DEFAULT_FIRST_NAME = "Ivan";

    private static final String DEFAULT_LAST_NAME = "Ivanov";

    private static final String DEFAULT_PATRONYMIC = "Ivanovich";

    private static final String DEFAULT_EMAIL = "test@example.com";

    private static final String LONG_STRING = "1".repeat(256);


    private UserUpdateMeRequestDto updateMeRequest;
    private UserUpdateMeResponseDto updateMeResponse;

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
                .thenThrow(AuthException.CODE.NO_SUCH_EMAIL_OR_PASSWORD.get());

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
                .thenThrow(UserException.CODE.NO_SUCH_USER.get());

        mockMvc.perform(put(PATH + "/{id}", DEFAULT_ID)
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(updateMeRequest)))
                .andExpect(status().isNotFound());
    }
    // The end of updateById method tests

}

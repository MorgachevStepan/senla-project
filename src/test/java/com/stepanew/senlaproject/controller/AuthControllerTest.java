package com.stepanew.senlaproject.controller;

import com.stepanew.senlaproject.config.SecurityConfiguration;
import com.stepanew.senlaproject.domain.dto.request.JwtRefreshRequestDto;
import com.stepanew.senlaproject.domain.dto.request.UserCreateRequestDto;
import com.stepanew.senlaproject.exceptions.AuthException;
import com.stepanew.senlaproject.security.dto.JwtRequest;
import com.stepanew.senlaproject.security.dto.JwtResponse;
import com.stepanew.senlaproject.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfiguration.class)
public class AuthControllerTest extends AbstractControllerTest {

    @MockBean
    private AuthService authService;

    private static final String PATH = "/api/v1/auth";

    private UserCreateRequestDto userCreateRequest;

    private JwtRequest jwtRequest;

    private JwtRefreshRequestDto jwtRefreshRequest;

    private JwtResponse jwtResponse;

    private static final String DEFAULT_PASSWORD = "1234";

    private static final String DEFAULT_REFRESH = "refresh token";

    private static final String DEFAULT_ACCESS = "access token";



    @BeforeEach
    void setUp() {
        userCreateRequest = new UserCreateRequestDto(
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_PATRONYMIC,
                "DEFAULT_PASSWORD",
                "DEFAULT_PASSWORD",
                DEFAULT_EMAIL
        );

        jwtRequest = new JwtRequest(
                DEFAULT_EMAIL,
                DEFAULT_PASSWORD
        );

        jwtRefreshRequest = new JwtRefreshRequestDto(
                DEFAULT_REFRESH
        );

        jwtResponse = new JwtResponse(
                DEFAULT_EMAIL,
                DEFAULT_ACCESS,
                DEFAULT_REFRESH
        );
    }

    //The beginning of register method tests
    // Test for POST /register - 200 OK
    @Test
    void registerTest() throws Exception {
        when(authService.register(any(UserCreateRequestDto.class))).thenReturn(jwtResponse);

        mockMvc.perform(post(PATH + "/register")
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtResponse)));
    }

    // Test 400 Bad Request
    @Test
    void registerBadRequestTest() throws Exception {
        UserCreateRequestDto invalidRequest = new UserCreateRequestDto(
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_PATRONYMIC,
                DEFAULT_PASSWORD,
                DEFAULT_PASSWORD.repeat(2),
                DEFAULT_EMAIL
        );

        when(authService.register(invalidRequest))
                .thenThrow(AuthException.CODE.INVALID_REPEAT_PASSWORD.get());

        mockMvc.perform(post(PATH + "/register")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
    //The end of register method tests

    //The beginning of login method tests
    //Test 200 OK
    @Test
    void loginTest() throws Exception {
        when(authService.login(any(JwtRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(post(PATH + "/login")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(jwtRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtResponse)));
    }

    //Test 400 Bad Request
    @Test
    void loginBadRequestTest() throws Exception {
        JwtRequest invalidRequest = new JwtRequest(
                DEFAULT_EMAIL,
                null
        );

        mockMvc.perform(post(PATH + "/login")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    //Test 404 Not Found
    @Test
    void loginNotFoundTest() throws Exception {
        when(authService.login(any(JwtRequest.class)))
                .thenThrow(AuthException.CODE.NO_SUCH_EMAIL_OR_PASSWORD.get());

        mockMvc.perform(post(PATH + "/login")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(jwtRequest)))
                .andExpect(status().isNotFound());
    }
    //The end of login method tests

    //The beginning of refresh method tests
    //Test 200 OK
    @Test
    void refreshTest() throws Exception {
        when(authService.refresh(any(JwtRefreshRequestDto.class))).thenReturn(jwtResponse);

        mockMvc.perform(post(PATH + "/refresh")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(jwtRefreshRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtResponse)));
    }

    // Test 401 Unauthorized
    @Test
    void refreshUnauthorizedTest() throws Exception {
        when(authService.refresh(any(JwtRefreshRequestDto.class)))
                .thenThrow(AuthException.CODE.JWT_VALIDATION_ERROR.get());

        mockMvc.perform(post(PATH + "/refresh")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(jwtRefreshRequest)))
                .andExpect(status().isUnauthorized());
    }

    //Test 404 Not Found
    @Test
    void refreshNotFoundTest() throws Exception {
        when(authService.refresh(any(JwtRefreshRequestDto.class)))
                .thenThrow(AuthException.CODE.NO_SUCH_EMAIL_OR_PASSWORD.get());

        mockMvc.perform(post(PATH + "/refresh")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(jwtRequest)))
                .andExpect(status().isNotFound());
    }
    //The end of login method tests

    //The beginning of getAccessToken method tests
    //Test 200 OK
    @Test
    void getAccessTokenTest() throws Exception {
        when(authService.getAccessToken(any(JwtRefreshRequestDto.class))).thenReturn(jwtResponse);

        mockMvc.perform(post(PATH + "/access")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(jwtRefreshRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtResponse)));
    }

    // Test 401 Unauthorized
    @Test
    void getAccessTokenUnauthorizedTest() throws Exception {
        when(authService.getAccessToken(any(JwtRefreshRequestDto.class)))
                .thenThrow(AuthException.CODE.JWT_VALIDATION_ERROR.get());

        mockMvc.perform(post(PATH + "/access")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(jwtRefreshRequest)))
                .andExpect(status().isUnauthorized());
    }

    //Test 404 Not Found
    @Test
    void getAccessTokenFoundTest() throws Exception {
        when(authService.getAccessToken(any(JwtRefreshRequestDto.class)))
                .thenThrow(AuthException.CODE.NO_SUCH_EMAIL_OR_PASSWORD.get());

        mockMvc.perform(post(PATH + "/access")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(jwtRefreshRequest)))
                .andExpect(status().isNotFound());
    }
    //The end of getAccessToken method tests

}

package com.stepanew.senlaproject.service;

import com.stepanew.senlaproject.domain.dto.request.JwtRefreshRequestDto;
import com.stepanew.senlaproject.domain.dto.request.UserCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.UserCreatedResponseDto;
import com.stepanew.senlaproject.domain.entity.User;
import com.stepanew.senlaproject.exceptions.AuthException;
import com.stepanew.senlaproject.security.dto.JwtRequest;
import com.stepanew.senlaproject.security.dto.JwtResponse;
import com.stepanew.senlaproject.security.jwt.JwtCore;
import com.stepanew.senlaproject.services.UserService;
import com.stepanew.senlaproject.services.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private JwtCore jwtCore;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthServiceImpl authService;

    private static final String DEFAULT_EMAIL = "test@example.com";

    private static final String DEFAULT_PASSWORD = "password";

    private static final String DEFAULT_REFRESH_TOKEN = "refresh_token";

    private static final String DEFAULT_ACCESS_TOKEN = "access_token";

    private static final String DEFAULT_FIRST_NAME = "Ivan";

    private static final String DEFAULT_LAST_NAME = "Ivanov";

    private static final String DEFAULT_PATRONYMIC = "Ivanovich";

    private static final Long USER_ID = 1L;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(USER_ID);
        user.setEmail(DEFAULT_EMAIL);
        user.setPassword(DEFAULT_PASSWORD);
    }

    @Test
    void loginTest() {
        JwtRequest loginRequest = new JwtRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);

        when(userService.getByEmail(DEFAULT_EMAIL)).thenReturn(user);
        when(jwtCore.createAccessToken(user)).thenReturn(DEFAULT_ACCESS_TOKEN);
        when(jwtCore.createRefreshToken(user)).thenReturn(DEFAULT_REFRESH_TOKEN);

        JwtResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals(DEFAULT_EMAIL, response.getEmail());
        assertEquals(DEFAULT_ACCESS_TOKEN, response.getAccessToken());
        assertEquals(DEFAULT_REFRESH_TOKEN, response.getRefreshToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).getByEmail(DEFAULT_EMAIL);
        verify(jwtCore).createAccessToken(user);
        verify(jwtCore).createRefreshToken(user);
    }

    @Test
    void refreshTest() {
        JwtRefreshRequestDto refreshRequest = new JwtRefreshRequestDto(DEFAULT_REFRESH_TOKEN);

        when(jwtCore.validateRefreshToken(DEFAULT_REFRESH_TOKEN)).thenReturn(true);
        when(jwtCore.getId(DEFAULT_REFRESH_TOKEN)).thenReturn(USER_ID.toString());
        when(userService.getById(USER_ID)).thenReturn(user);
        when(jwtCore.createAccessToken(user)).thenReturn(DEFAULT_ACCESS_TOKEN);
        when(jwtCore.createRefreshToken(user)).thenReturn(DEFAULT_REFRESH_TOKEN);

        JwtResponse response = authService.refresh(refreshRequest);

        assertNotNull(response);
        assertEquals(DEFAULT_EMAIL, response.getEmail());
        assertEquals(DEFAULT_ACCESS_TOKEN, response.getAccessToken());
        assertEquals(DEFAULT_REFRESH_TOKEN, response.getRefreshToken());

        verify(jwtCore).validateRefreshToken(DEFAULT_REFRESH_TOKEN);
        verify(jwtCore).getId(DEFAULT_REFRESH_TOKEN);
        verify(userService).getById(USER_ID);
        verify(jwtCore).createAccessToken(user);
        verify(jwtCore).createRefreshToken(user);
    }

    @Test
    void refreshThrowsExceptionTest() {
        JwtRefreshRequestDto refreshRequest = new JwtRefreshRequestDto(DEFAULT_REFRESH_TOKEN);

        when(jwtCore.validateRefreshToken(DEFAULT_REFRESH_TOKEN)).thenReturn(false);

        AuthException exception = assertThrows(AuthException.class, () -> authService.refresh(refreshRequest));
        assertEquals(AuthException.CODE.JWT_VALIDATION_ERROR, exception.getCode());

        verify(jwtCore).validateRefreshToken(DEFAULT_REFRESH_TOKEN);
        verifyNoMoreInteractions(jwtCore, userService);
    }

    @Test
    void registerTest() {
        UserCreateRequestDto registerRequest = new UserCreateRequestDto(
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_PATRONYMIC,
                DEFAULT_PASSWORD,
                DEFAULT_PASSWORD,
                DEFAULT_EMAIL
        );

        UserCreatedResponseDto userCreatedResponseDto = new UserCreatedResponseDto(
                USER_ID,
                DEFAULT_EMAIL,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_PATRONYMIC
        );


        when(userService.create(registerRequest)).thenReturn(userCreatedResponseDto);
        when(userService.getByEmail(DEFAULT_EMAIL)).thenReturn(user);
        when(jwtCore.createAccessToken(user)).thenReturn(DEFAULT_ACCESS_TOKEN);
        when(jwtCore.createRefreshToken(user)).thenReturn(DEFAULT_REFRESH_TOKEN);

        JwtResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals(DEFAULT_EMAIL, response.getEmail());
        assertEquals(DEFAULT_ACCESS_TOKEN, response.getAccessToken());
        assertEquals(DEFAULT_REFRESH_TOKEN, response.getRefreshToken());

        verify(userService).create(registerRequest);
        verify(userService).getByEmail(DEFAULT_EMAIL);
        verify(jwtCore).createAccessToken(user);
        verify(jwtCore).createRefreshToken(user);
    }

    @Test
    void getAccessTokenTest() {
        JwtRefreshRequestDto refreshRequest = new JwtRefreshRequestDto(DEFAULT_REFRESH_TOKEN);

        when(jwtCore.validateRefreshToken(DEFAULT_REFRESH_TOKEN)).thenReturn(true);
        when(jwtCore.getId(DEFAULT_REFRESH_TOKEN)).thenReturn(USER_ID.toString());
        when(userService.getById(USER_ID)).thenReturn(user);
        when(jwtCore.createAccessToken(user)).thenReturn(DEFAULT_ACCESS_TOKEN);

        JwtResponse response = authService.getAccessToken(refreshRequest);

        assertNotNull(response);
        assertEquals(DEFAULT_EMAIL, response.getEmail());
        assertEquals(DEFAULT_ACCESS_TOKEN, response.getAccessToken());
        assertEquals(DEFAULT_REFRESH_TOKEN, response.getRefreshToken());

        verify(jwtCore).validateRefreshToken(DEFAULT_REFRESH_TOKEN);
        verify(jwtCore).getId(DEFAULT_REFRESH_TOKEN);
        verify(userService).getById(USER_ID);
        verify(jwtCore).createAccessToken(user);
    }
}

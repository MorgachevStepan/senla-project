package com.stepanew.senlaproject.services.impl;

import com.stepanew.senlaproject.domain.dto.request.JwtRefreshRequestDto;
import com.stepanew.senlaproject.domain.dto.request.UserCreateRequestDto;
import com.stepanew.senlaproject.domain.entity.User;
import com.stepanew.senlaproject.domain.enums.TokenType;
import com.stepanew.senlaproject.exceptions.AuthException;
import com.stepanew.senlaproject.security.dto.JwtRequest;
import com.stepanew.senlaproject.security.dto.JwtResponse;
import com.stepanew.senlaproject.security.jwt.JwtCore;
import com.stepanew.senlaproject.services.AuthService;
import com.stepanew.senlaproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtCore jwtCore;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        final User user = userService.getByEmail(loginRequest.getEmail());
        return createJwtResponse(user);
    }

    @Override
    public JwtResponse refresh(JwtRefreshRequestDto refreshTokenDto) {
        return generateAccessOrRefreshToken(refreshTokenDto.refreshToken(), TokenType.REFRESH);
    }

    @Override
    public JwtResponse register(UserCreateRequestDto registerRequest) {
        User user = userService
                .getByEmail(userService.create(registerRequest).email());
        return createJwtResponse(user);
    }

    @Override
    public JwtResponse getAccessToken(JwtRefreshRequestDto refreshTokenDto) {
        return generateAccessOrRefreshToken(refreshTokenDto.refreshToken(), TokenType.ACCESS);
    }

    private JwtResponse generateAccessOrRefreshToken(String refreshToken, TokenType tokenType) {
        if (!jwtCore.validateRefreshToken(refreshToken)) {
            throw AuthException.CODE.JWT_VALIDATION_ERROR.get();
        }

        Long id = Long.valueOf(jwtCore.getId(refreshToken));
        User user = userService.getById(id);

        if (tokenType == TokenType.ACCESS) {
            return createJwtResponse(user, refreshToken);
        } else {
            return createJwtResponse(user);
        }
    }

    private JwtResponse createJwtResponse(User user) {
        return JwtResponse
                .builder()
                .email(user.getEmail())
                .accessToken(jwtCore.createAccessToken(user))
                .refreshToken(jwtCore.createRefreshToken(user))
                .build();
    }

    private JwtResponse createJwtResponse(User user, String refreshToken) {
        return JwtResponse
                .builder()
                .email(user.getEmail())
                .accessToken(jwtCore.createAccessToken(user))
                .refreshToken(refreshToken)
                .build();
    }

}

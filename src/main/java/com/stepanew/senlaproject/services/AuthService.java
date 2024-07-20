package com.stepanew.senlaproject.services;

import com.stepanew.senlaproject.domain.dto.request.JwtRefreshRequestDto;
import com.stepanew.senlaproject.domain.dto.request.UserCreateRequestDto;
import com.stepanew.senlaproject.security.dto.JwtRequest;
import com.stepanew.senlaproject.security.dto.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(JwtRefreshRequestDto refreshToken);

    JwtResponse register(UserCreateRequestDto registerRequest);

    JwtResponse getAccessToken(JwtRefreshRequestDto refreshToken);

}

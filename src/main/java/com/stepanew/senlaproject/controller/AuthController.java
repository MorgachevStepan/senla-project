package com.stepanew.senlaproject.controller;

import com.stepanew.senlaproject.controller.api.AuthApi;
import com.stepanew.senlaproject.domain.dto.request.JwtRefreshRequestDto;
import com.stepanew.senlaproject.domain.dto.request.UserCreateRequestDto;
import com.stepanew.senlaproject.security.dto.JwtRequest;
import com.stepanew.senlaproject.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Validated UserCreateRequestDto request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated JwtRequest loginRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody JwtRefreshRequestDto refreshToken){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.refresh(refreshToken));
    }

    @PostMapping("/access")
    public ResponseEntity<?> getAccessToken(@RequestBody JwtRefreshRequestDto refreshToken){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.getAccessToken(refreshToken));
    }

}

package com.stepanew.senlaproject.controller.api;

import com.stepanew.senlaproject.domain.dto.request.JwtRefreshRequestDto;
import com.stepanew.senlaproject.domain.dto.request.UserCreateRequestDto;
import com.stepanew.senlaproject.exceptions.message.ErrorMessage;
import com.stepanew.senlaproject.security.dto.JwtRequest;
import com.stepanew.senlaproject.security.dto.JwtResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth API", description = "API для входа в систему")
public interface AuthApi {

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный вход в аккаунт",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JwtResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверные данные пользователя",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь по переданному email не был найден",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Login -> возвращает access и refresh token")
    ResponseEntity<?> login(
            @RequestBody JwtRequest jwtRequest
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешная регистрация",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JwtResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пароли не совпадают или пользователь с таким именем существует",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Register -> возвращает access и refresh token")
    ResponseEntity<?> register(
            @RequestBody UserCreateRequestDto request
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное обновление jwt токенов",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JwtResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Ошибка валидации jwt токена",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь, указанный в токене не был найден",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Возвращает обновленные access и refresh токены")
    ResponseEntity<?> refresh(
            @RequestBody JwtRefreshRequestDto request
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное обновление jwt токенов",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JwtResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Ошибка валидации jwt токена",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь, указанный в токене не был найден",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Возвращает обновленный access токен")
    ResponseEntity<?> getAccessToken(
            @RequestBody JwtRefreshRequestDto request
    );

}

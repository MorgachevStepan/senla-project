package com.stepanew.senlaproject.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "Email и пароль")
@AllArgsConstructor
public class JwtRequest {

    @Schema(description = "email пользователя", example = "test@test.com")
    private String email;

    @Schema(description = "пароль", example = "1234")
    private String password;

}

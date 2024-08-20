package com.stepanew.senlaproject.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Форма для добавления роли админа для пользователя")
public record UserAddRoleRequestDto(

        @NotNull(message = "Email must be not null")
        @Email(message = "Wrong email format")
        @Schema(description = "Email", example = "examle@examle.com")
        String email

) {
}

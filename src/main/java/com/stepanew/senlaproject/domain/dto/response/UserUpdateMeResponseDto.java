package com.stepanew.senlaproject.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserUpdateMeResponseDto(

        @Schema(description = "Email пользователя", example = "foo@foo.com")
        String email,

        @Schema(description = "Имя пользователя", example = "Иван")
        String firstName,

        @Schema(description = "Фамилия пользователя", example = "Иванов")
        String lastName,

        @Schema(description = "Отчество пользователя", example = "Иванович")
        String patronymic

) {
}

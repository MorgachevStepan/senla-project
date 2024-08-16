package com.stepanew.senlaproject.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Форма для регистрации")
public record UserCreateRequestDto(
        @NotNull(message = "Firstname must be not null")
        @Length(max = 255, message = "Firstname length must be smaller than 255 symbols")
        @Schema(description = "Имя", example = "Иван")
        String firstName,

        @NotNull(message = "Lastname must be not null")
        @Length(max = 255, message = "Lastname length must be smaller than 255 symbols")
        @Schema(description = "Фамилия", example = "Иванов")
        String lastName,

        @Length(max = 255, message = "Patronymic length must be smaller than 255 symbols")
        @Schema(description = "Отчество", example = "Иванович")
        String patronymic,

        @NotNull(message = "Password must be not null")
        @Schema(description = "Пароль", example = "1234")
        String password,

        @NotNull(message = "Confirmation must be not null")
        @Schema(description = "Повтор пароля", example = "1234")
        String repeatPassword,

        @NotNull(message = "Email must be not null")
        @Email(message = "Wrong email format")
        @Schema(description = "Email", example = "example@example.com")
        String email
) {
}

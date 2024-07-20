package com.stepanew.senlaproject.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserCreateRequestDto(
        @NotNull(message = "Firstname must be not null")
        @Length(max = 255, message = "Firstname length must be smaller than 255 symbols")
        String firstName,

        @NotNull(message = "Lastname must be not null")
        @Length(max = 255, message = "Lastname length must be smaller than 255 symbols")
        String lastName,

        @Length(max = 255, message = "Patronymic length must be smaller than 255 symbols")
        String patronymic,

        @NotNull(message = "Password must be not null")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password,

        @NotNull(message = "Confirmation must be not null")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String repeatPassword,

        @NotNull(message = "Email must be not null")
        @Email(message = "Wrong email format")
        String email
) {
}

package com.stepanew.senlaproject.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.validator.constraints.Length;

@Tag(name = "Форма для обновления собственного профиля")
public record UserUpdateMeRequestDto(

        @Length(max = 255, message = "Firstname length must be smaller than 255 symbols")
        @Schema(description = "Имя", example = "Иван")
        String firstName,

        @Length(max = 255, message = "Lastname length must be smaller than 255 symbols")
        @Schema(description = "Фамилия", example = "Иванов")
        String lastName,

        @Length(max = 255, message = "Patronymic length must be smaller than 255 symbols")
        @Schema(description = "Отчество", example = "Иванович")
        String patronymic

) {
}

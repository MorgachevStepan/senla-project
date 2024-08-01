package com.stepanew.senlaproject.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Форма для создания категории")
public record CategoryCreateRequestDto(

        @NotNull(message = "Name must be not null")
        @Length(max = 255, message = "Name length must be smaller than 255 symbols")
        @Schema(description = "Название категории", example = "Мясо")
        String name,

        @Length(max = 255, message = "Description length must be smaller than 255 symbols")
        @Schema(description = "Описание категории", example = "Мясные продукты")
        String description

) {
}

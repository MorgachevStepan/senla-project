package com.stepanew.senlaproject.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ProductCreateRequestDto(

        @NotNull(message = "CategoryId must be not null")
        @Min(value = 1, message = "CategoryId must be than 0")
        @Schema(description = "ID категории", example = "1")
        Long categoryId,

        @NotNull(message = "Name must be not null")
        @Length(max = 255, message = "Name length must be smaller than 255 symbols")
        @Schema(description = "Наименование продукта", example = "Яблоко")
        String name,

        @Length(max = 255, message = "Description length must be smaller than 255 symbols")
        @Schema(description = "Описание продукта", example = "Фрукты, ягоды")
        String description

) {
}

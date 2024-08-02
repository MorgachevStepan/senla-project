package com.stepanew.senlaproject.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ProductUpdateRequestDto(

        @NotNull(message = "Id must be not null")
        @Schema(description = "ID продукта", example = "1")
        Long id,

        @Length(max = 255, message = "Name length must be smaller than 255 symbols")
        @Schema(description = "Наименование продукта", example = "Яблоко")
        String name,

        @Length(max = 255, message = "Description length must be smaller than 255 symbols")
        @Schema(description = "Описание продукта", example = "Фрукты, ягоды")
        String description

) {
}

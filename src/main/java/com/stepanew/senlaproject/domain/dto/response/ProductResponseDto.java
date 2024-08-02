package com.stepanew.senlaproject.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductResponseDto(

        @Schema(description = "ID продукта", example = "1")
        Long id,

        @Schema(description = "Наименование продукта", example = "Яблоко")
        String name,

        @Schema(description = "Описание продукта", example = "Свежие яблоки, 1 кг")
        String description,

        @Schema(description = "Название категории", example = "Фрукты, ягоды")
        String category

) {
}

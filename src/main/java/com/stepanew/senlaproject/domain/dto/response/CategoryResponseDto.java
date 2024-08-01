package com.stepanew.senlaproject.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryResponseDto(

        @Schema(description = "ID категории", example = "1")
        Long id,

        @Schema(description = "Название категории", example = "Мясо")
        String name,

        @Schema(description = "Описание категории", example = "Мясные продукты")
        String description

) {
}

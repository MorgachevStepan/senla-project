package com.stepanew.senlaproject.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record PriceComparisonResponseDto(

        @Schema(description = "Список сравнения товаров")
        List<ComparisonResponseDto> comparison

) {
}

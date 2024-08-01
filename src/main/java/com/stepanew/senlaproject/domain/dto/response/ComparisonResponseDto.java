package com.stepanew.senlaproject.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ComparisonResponseDto(

        @Schema(description = "ID продукта", example = "1")
        Long productId,

        @Schema(description = "Название продукта", example = "Молоко")
        String productName,

        @Schema(description = "Минимальная цена")
        StoreWithPriceResponseDto minPrice,

        @Schema(description = "Максимальная цена")
        StoreWithPriceResponseDto maxPrice,

        @Schema(description = "Статус сравнения", example = "В магазине Example по адресу Example дешевле чем в магазине Example2 по адресу %Example2 на 10 рублей или на 10 процентов")
        String status

) {
}

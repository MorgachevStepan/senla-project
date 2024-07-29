package com.stepanew.senlaproject.domain.dto.response;

public record ComparisonResponseDto(

        Long productId,

        String productName,

        StoreWithPriceResponseDto minPrice,

        StoreWithPriceResponseDto maxPrice,

        String status

) {
}

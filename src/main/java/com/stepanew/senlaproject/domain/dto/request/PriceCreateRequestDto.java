package com.stepanew.senlaproject.domain.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PriceCreateRequestDto(

        @NotNull(message = "ProductId must be not null")
        @Min(value = 1, message = "ProductId must be than 0")
        Long productId,

        @NotNull(message = "StoreId must be not null")
        @Min(value = 1, message = "StoreId must be than 0")
        Long storeId,

        @NotNull(message = "Price must be not null")
        BigDecimal price

) {
}

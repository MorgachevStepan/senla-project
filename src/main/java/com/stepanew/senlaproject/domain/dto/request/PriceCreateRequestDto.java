package com.stepanew.senlaproject.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PriceCreateRequestDto(

        @NotNull(message = "ProductId must be not null")
        @Min(value = 1, message = "ProductId must be than 0")
        @Schema(description = "ID продукта", example = "1")
        Long productId,

        @NotNull(message = "StoreId must be not null")
        @Min(value = 1, message = "StoreId must be than 0")
        @Schema(description = "ID торговой точки", example = "1")
        Long storeId,

        @NotNull(message = "Price must be not null")
        @Schema(description = "Стоимость продукта", example = "99.99")
        BigDecimal price

) {
}

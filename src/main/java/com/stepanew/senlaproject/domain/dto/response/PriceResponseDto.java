package com.stepanew.senlaproject.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceResponseDto(

        @Schema(description = "ID продукта", example = "1")
        Long productId,

        @Schema(description = "ID магазина", example = "1")
        Long storeId,

        @Schema(description = "Цена на товар в данном магазине", example = "99.99")
        BigDecimal price,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
        @Schema(description = "Время установки цены", example = "01.01.2024 12:00:00")
        LocalDateTime checkedDate

) {
}

package com.stepanew.senlaproject.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record StoreWithPriceResponseDto(

        @Schema(description = "ID магазина", example = "1")
        Long id,

        @Schema(description = "Название магазина", example = "Пятёрочка")
        String name,

        @Schema(description = "Адрес магазина", example = "Город, ул. Улица, д. Дом1")
        String address,

        @Schema(description = "Цена товара в данном магазине", example = "22.99")
        BigDecimal price

) {
}

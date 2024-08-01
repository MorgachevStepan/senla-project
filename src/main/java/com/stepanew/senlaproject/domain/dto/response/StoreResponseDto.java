package com.stepanew.senlaproject.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record StoreResponseDto(

        @Schema(description = "ID торговой точки", example = "1")
        Long id,

        @Schema(description = "Название торговой точки", example = "Пятёрочка")
        String name,

        @Schema(description = "Телефонный номер торговой точки", example = "88005553535")
        String number,

        @Schema(description = "Адрес торговой точки", example = "Город, ул. Улица, д. Дом1")
        String address

) {
}

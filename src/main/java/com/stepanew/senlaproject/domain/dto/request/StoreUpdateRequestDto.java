package com.stepanew.senlaproject.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record StoreUpdateRequestDto(

        @NotNull(message = "Id must be not null")
        @Schema(description = "ID торговой точки", example = "1")
        Long id,

        @Length(max = 255, message = "Name length must be smaller than 255 symbols")
        @Schema(description = "Название торговой точки", example = "Пятёрочка")
        String name,

        @Pattern(regexp = "^[78]\\d{10}$", message = "Phone number must be 11 digits and start with 7 or 8")
        @Schema(description = "Телефонный номер торговой точки", example = "88005553535")
        String number,

        @Length(max = 255, message = "Address length must be smaller than 255 symbols")
        @Schema(description = "Адрес торговой точки", example = "Город, ул. Улица, д. Дом1")
        String address

) {
}

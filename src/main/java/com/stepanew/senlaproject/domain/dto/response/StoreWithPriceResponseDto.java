package com.stepanew.senlaproject.domain.dto.response;

import java.math.BigDecimal;

public record StoreWithPriceResponseDto(

        Long id,

        String name,

        String address,

        BigDecimal price

) {
}

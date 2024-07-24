package com.stepanew.senlaproject.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceResponseDto(

        Long productId,

        Long storeId,

        BigDecimal price,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
        LocalDateTime checkedDate

) {
}

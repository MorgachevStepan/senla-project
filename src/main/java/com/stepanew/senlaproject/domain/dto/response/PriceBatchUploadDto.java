package com.stepanew.senlaproject.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record PriceBatchUploadDto(

        @Schema(description = "Список данных о добавлении цен на продукты")
        List<PriceResponseDto> uploaded

) {
}

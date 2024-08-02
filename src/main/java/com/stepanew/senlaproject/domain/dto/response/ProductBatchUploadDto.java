package com.stepanew.senlaproject.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ProductBatchUploadDto(

        @Schema(description = "Список DTO с добавлением продуктов")
        List<ProductResponseDto> uploaded

) {
}

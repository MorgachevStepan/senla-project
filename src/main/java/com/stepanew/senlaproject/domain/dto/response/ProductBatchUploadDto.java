package com.stepanew.senlaproject.domain.dto.response;

import java.util.List;

public record ProductBatchUploadDto(

        List<ProductResponseDto> uploaded

) {
}

package com.stepanew.senlaproject.domain.dto.response;

import java.util.List;

public record PriceBatchUploadDto(

        List<PriceResponseDto> uploaded

) {
}

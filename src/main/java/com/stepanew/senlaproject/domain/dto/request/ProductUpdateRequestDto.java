package com.stepanew.senlaproject.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ProductUpdateRequestDto(

        @NotNull(message = "Id must be not null")
        Long id,

        @Length(max = 255, message = "Name length must be smaller than 255 symbols")
        String name,

        @Length(max = 255, message = "Description length must be smaller than 255 symbols")
        String description

) {
}

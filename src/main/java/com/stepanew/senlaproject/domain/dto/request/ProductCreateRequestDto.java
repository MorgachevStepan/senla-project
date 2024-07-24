package com.stepanew.senlaproject.domain.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ProductCreateRequestDto(

        @NotNull(message = "CategoryId must be not null")
        @Min(value = 1, message = "CategoryId must be than 0")
        Long categoryId,

        @NotNull(message = "Name must be not null")
        @Length(max = 255, message = "Name length must be smaller than 255 symbols")
        String name,

        @Length(max = 255, message = "Description length must be smaller than 255 symbols")
        String description

) {
}

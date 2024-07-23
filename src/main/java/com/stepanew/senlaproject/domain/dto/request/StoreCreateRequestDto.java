package com.stepanew.senlaproject.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record StoreCreateRequestDto(

        @NotNull(message = "Name must be not null")
        @Length(max = 255, message = "Name length must be smaller than 255 symbols")
        String name,

        @Pattern(regexp = "^[78]\\d{10}$", message = "Phone number must be 11 digits and start with 7 or 8")
        String number,

        @NotNull(message = "Address must be not null")
        @Length(max = 255, message = "Address length must be smaller than 255 symbols")
        String address

) {
}

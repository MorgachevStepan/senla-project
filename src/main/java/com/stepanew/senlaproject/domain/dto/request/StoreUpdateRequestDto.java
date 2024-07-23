package com.stepanew.senlaproject.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record StoreUpdateRequestDto(

        @NotNull(message = "Id must be not null")
        Long id,

        @Length(max = 255, message = "Name length must be smaller than 255 symbols")
        String name,

        @Pattern(regexp = "^[78]\\d{10}$", message = "Phone number must be 11 digits and start with 7 or 8")
        String number,

        @Length(max = 255, message = "Address length must be smaller than 255 symbols")
        String address

) {
}

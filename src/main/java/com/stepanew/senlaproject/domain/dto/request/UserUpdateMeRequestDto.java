package com.stepanew.senlaproject.domain.dto.request;

import org.hibernate.validator.constraints.Length;

public record UserUpdateMeRequestDto(

        @Length(max = 255, message = "Firstname length must be smaller than 255 symbols")
        String firstName,

        @Length(max = 255, message = "Lastname length must be smaller than 255 symbols")
        String lastName,

        @Length(max = 255, message = "Patronymic length must be smaller than 255 symbols")
        String patronymic

) {
}

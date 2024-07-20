package com.stepanew.senlaproject.domain.dto.response;

public record UserUpdateMeResponseDto(

        String email,

        String firstName,

        String lastName,

        String patronymic

) {
}

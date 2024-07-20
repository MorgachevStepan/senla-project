package com.stepanew.senlaproject.domain.dto.response;

public record UserCreatedResponseDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String patronymic
) {
}

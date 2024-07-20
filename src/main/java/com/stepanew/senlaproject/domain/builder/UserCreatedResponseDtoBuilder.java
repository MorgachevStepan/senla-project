package com.stepanew.senlaproject.domain.builder;

import com.stepanew.senlaproject.domain.dto.response.UserCreatedResponseDto;
import com.stepanew.senlaproject.domain.entity.User;

public class UserCreatedResponseDtoBuilder {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String patronymic;

    public UserCreatedResponseDtoBuilder() {
    }

    private UserCreatedResponseDtoBuilder id(Long id) {
        this.id = id;
        return this;
    }

    private UserCreatedResponseDtoBuilder email(String email) {
        this.email = email;
        return this;
    }

    private UserCreatedResponseDtoBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    private UserCreatedResponseDtoBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    private UserCreatedResponseDtoBuilder patronymic(String patronymic) {
        this.patronymic = patronymic;
        return this;
    }

    private UserCreatedResponseDto build() {
        return new UserCreatedResponseDto(id, email, firstName, lastName, patronymic);
    }

    public UserCreatedResponseDto buildUserCreatedResponseDto(User user) {
        return new UserCreatedResponseDtoBuilder()
                .firstName(user.getProfile().getFirstName())
                .lastName(user.getProfile().getLastName())
                .patronymic(user.getProfile().getPatronymic())
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }

}

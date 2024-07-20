package com.stepanew.senlaproject.domain.builder;

import com.stepanew.senlaproject.domain.dto.response.UserUpdateMeResponseDto;
import com.stepanew.senlaproject.domain.entity.User;

public class UserUpdateMeResponseDtoBuilder {

    private String email;
    private String firstName;
    private String lastName;
    private String patronymic;

    public UserUpdateMeResponseDtoBuilder() {

    }

    private UserUpdateMeResponseDtoBuilder email(String email) {
        this.email = email;
        return this;
    }

    private UserUpdateMeResponseDtoBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    private UserUpdateMeResponseDtoBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    private UserUpdateMeResponseDtoBuilder patronymic(String patronymic) {
        this.patronymic = patronymic;
        return this;
    }

    private UserUpdateMeResponseDto build() {
        return new UserUpdateMeResponseDto(email, firstName, lastName, patronymic);
    }

    public UserUpdateMeResponseDto buildUserUpdateMeResponseDto(User user) {
        return new UserUpdateMeResponseDtoBuilder()
                .firstName(user.getProfile().getFirstName())
                .lastName(user.getProfile().getLastName())
                .patronymic(user.getProfile().getPatronymic())
                .email(user.getEmail())
                .build();
    }

}

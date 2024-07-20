package com.stepanew.senlaproject.domain.builder;

import com.stepanew.senlaproject.domain.dto.response.UserCreatedResponseDto;

public class UserCreatedResponseDtoBuilder {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String patronymic;

    public UserCreatedResponseDtoBuilder() {
    }

    public UserCreatedResponseDtoBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public UserCreatedResponseDtoBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserCreatedResponseDtoBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserCreatedResponseDtoBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserCreatedResponseDtoBuilder patronymic(String patronymic) {
        this.patronymic = patronymic;
        return this;
    }

    public UserCreatedResponseDto build() {
        return new UserCreatedResponseDto(id, email, firstName, lastName, patronymic);
    }

}

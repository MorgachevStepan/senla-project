package com.stepanew.senlaproject.services;

import com.stepanew.senlaproject.domain.dto.request.UserAddRoleRequestDto;
import com.stepanew.senlaproject.domain.dto.request.UserCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.UserUpdateMeRequestDto;
import com.stepanew.senlaproject.domain.dto.response.UserAddRoleResponseDto;
import com.stepanew.senlaproject.domain.dto.response.UserCreatedResponseDto;
import com.stepanew.senlaproject.domain.dto.response.UserUpdateMeResponseDto;
import com.stepanew.senlaproject.domain.entity.User;

public interface UserService {

    User getById(Long id);

    User getByEmail(String email);

    UserCreatedResponseDto create(UserCreateRequestDto request);

    UserUpdateMeResponseDto updateMe(UserUpdateMeRequestDto request, String email);

    UserUpdateMeResponseDto updateById(UserUpdateMeRequestDto request, Long id);

    UserAddRoleResponseDto addAdminRole(UserAddRoleRequestDto request);

}

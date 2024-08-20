package com.stepanew.senlaproject.services;

import com.stepanew.senlaproject.domain.dto.request.UserAddRoleRequestDto;
import com.stepanew.senlaproject.domain.dto.request.UserCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.UserUpdateMeRequestDto;
import com.stepanew.senlaproject.domain.dto.response.UserAddRoleResponseDto;
import com.stepanew.senlaproject.domain.dto.response.UserCreatedResponseDto;
import com.stepanew.senlaproject.domain.dto.response.UserUpdateMeResponseDto;
import com.stepanew.senlaproject.domain.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    @Transactional(readOnly = true)
    User getById(Long id);

    @Transactional(readOnly = true)
    User getByEmail(String email);

    @Transactional(readOnly = false)
    UserCreatedResponseDto create(UserCreateRequestDto request);

    @Transactional(readOnly = false)
    UserUpdateMeResponseDto updateMe(UserUpdateMeRequestDto request, String email);

    @Transactional(readOnly = false)
    UserUpdateMeResponseDto updateById(UserUpdateMeRequestDto request, Long id);

    @Transactional(readOnly = false)
    UserAddRoleResponseDto addAdminRole(UserAddRoleRequestDto request);

}

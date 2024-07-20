package com.stepanew.senlaproject.services;

import com.stepanew.senlaproject.domain.dto.request.UserCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.UserCreatedResponseDto;
import com.stepanew.senlaproject.domain.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    @Transactional(readOnly = true)
    User getById(Long id);

    @Transactional(readOnly = true)
    User getByEmail(String email);

    @Transactional(readOnly = false)
    UserCreatedResponseDto create(UserCreateRequestDto request);

}

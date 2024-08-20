package com.stepanew.senlaproject.services.impl;

import com.stepanew.senlaproject.domain.dto.request.UserAddRoleRequestDto;
import com.stepanew.senlaproject.domain.dto.request.UserCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.UserUpdateMeRequestDto;
import com.stepanew.senlaproject.domain.dto.response.UserAddRoleResponseDto;
import com.stepanew.senlaproject.domain.dto.response.UserCreatedResponseDto;
import com.stepanew.senlaproject.domain.dto.response.UserUpdateMeResponseDto;
import com.stepanew.senlaproject.domain.entity.Profile;
import com.stepanew.senlaproject.domain.entity.Role;
import com.stepanew.senlaproject.domain.entity.User;
import com.stepanew.senlaproject.domain.enums.RoleType;
import com.stepanew.senlaproject.domain.mapper.profile.CreateProfileRequestDtoMapper;
import com.stepanew.senlaproject.domain.mapper.user.UserCreateRequestDtoMapper;
import com.stepanew.senlaproject.domain.mapper.user.UserCreateResponseDtoMapper;
import com.stepanew.senlaproject.domain.mapper.user.UserUpdateMeResponseDtoMapper;
import com.stepanew.senlaproject.exceptions.AuthException;
import com.stepanew.senlaproject.exceptions.UserException;
import com.stepanew.senlaproject.repository.UserRepository;
import com.stepanew.senlaproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserCreateRequestDtoMapper userCreateRequestDtoMapper;

    private final CreateProfileRequestDtoMapper createProfileRequestDtoMapper;

    private final UserUpdateMeResponseDtoMapper userUpdateMeResponseDtoMapper;

    private final UserCreateResponseDtoMapper userCreateResponseDtoMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User getById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(UserException.CODE.NO_SUCH_USER_ID::get);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(UserException.CODE.NO_SUCH_USER_EMAIL::get);
    }

    @Override
    public UserCreatedResponseDto create(UserCreateRequestDto request) {
        User user = userCreateRequestDtoMapper.toEntity(request);

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw AuthException.CODE.EMAIL_IN_USE.get();
        }

        if (!request.password().equals(request.repeatPassword())) {
            throw AuthException.CODE.INVALID_REPEAT_PASSWORD.get();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = Set.of(new Role(2L, RoleType.ROLE_USER));
        user.setRoles(roles);
        Profile profile = createProfileRequestDtoMapper.toEntity(request);
        user.setProfile(profile);
        profile.setUser(user);
        userRepository.save(user);

        return userCreateResponseDtoMapper.toDto(user);
    }

    @Override
    public UserUpdateMeResponseDto updateMe(UserUpdateMeRequestDto request, String email) {
        User updatedUser = getByEmail(email);
        return update(updatedUser, request);
    }

    @Override
    public UserUpdateMeResponseDto updateById(UserUpdateMeRequestDto request, Long id) {
        User updatedUser = getById(id);

        return update(updatedUser, request);
    }

    @Override
    public UserAddRoleResponseDto addAdminRole(UserAddRoleRequestDto request) {
        User updatedUser = getByEmail(request.email());

        if (updatedUser.getRoles().contains(new Role(1L, RoleType.ROLE_ADMIN))) {
            throw UserException.CODE.USER_IS_ALREADY_ADMIN.get();
        }

        updatedUser.getRoles().add(new Role(1L, RoleType.ROLE_ADMIN));

        userRepository.save(updatedUser);

        return new UserAddRoleResponseDto(request.email());
    }

    private UserUpdateMeResponseDto update(User updatedUser, UserUpdateMeRequestDto request) {
        Profile profile = updatedUser.getProfile();

        if (request.firstName() != null) {
            profile.setFirstName(request.firstName());
        }

        if (request.lastName() != null) {
            profile.setLastName(request.lastName());
        }

        if (request.patronymic() != null) {
            profile.setPatronymic(request.patronymic());
        }

        userRepository.save(updatedUser);

        return userUpdateMeResponseDtoMapper.toDto(updatedUser);
    }

}

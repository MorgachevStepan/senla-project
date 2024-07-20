package com.stepanew.senlaproject.services.impl;

import com.stepanew.senlaproject.domain.builder.UserCreatedResponseDtoBuilder;
import com.stepanew.senlaproject.domain.dto.request.UserCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.UserCreatedResponseDto;
import com.stepanew.senlaproject.domain.entity.Profile;
import com.stepanew.senlaproject.domain.entity.Role;
import com.stepanew.senlaproject.domain.entity.User;
import com.stepanew.senlaproject.domain.mapper.profile.CreateProfileRequestDtoMapper;
import com.stepanew.senlaproject.domain.mapper.user.CreateUserRequestDtoMapper;
import com.stepanew.senlaproject.exceptions.AuthException;
import com.stepanew.senlaproject.exceptions.UserException;
import com.stepanew.senlaproject.repository.UserRepository;
import com.stepanew.senlaproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final CreateUserRequestDtoMapper createUserRequestDtoMapper;

    private final CreateProfileRequestDtoMapper createProfileRequestDtoMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User getById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(UserException.CODE.NO_SUCH_USER::get);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(AuthException.CODE.NO_SUCH_EMAIL_OR_PASSWORD::get);
    }

    @Override
    public UserCreatedResponseDto create(UserCreateRequestDto request) {
        User user = createUserRequestDtoMapper.toEntity(request);

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw AuthException.CODE.EMAIL_IN_USE.get();
        }

        if (!request.password().equals(request.repeatPassword())) {
            throw AuthException.CODE.INVALID_REPEAT_PASSWORD.get();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedDate(LocalDateTime.now());
        Set<Role> roles = Set.of(new Role(2L, "ROLE_USER"));
        user.setRoles(roles);
        Profile profile = createProfileRequestDtoMapper.toEntity(request);
        user.setProfile(profile);
        profile.setUser(user);
        userRepository.save(user);

        return new UserCreatedResponseDtoBuilder()
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .patronymic(profile.getPatronymic())
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }

}

package com.stepanew.senlaproject.service;

import com.stepanew.senlaproject.domain.dto.request.UserCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.UserUpdateMeRequestDto;
import com.stepanew.senlaproject.domain.dto.response.UserCreatedResponseDto;
import com.stepanew.senlaproject.domain.dto.response.UserUpdateMeResponseDto;
import com.stepanew.senlaproject.domain.entity.Profile;
import com.stepanew.senlaproject.domain.entity.User;
import com.stepanew.senlaproject.domain.mapper.profile.CreateProfileRequestDtoMapper;
import com.stepanew.senlaproject.domain.mapper.user.UserCreateRequestDtoMapper;
import com.stepanew.senlaproject.domain.mapper.user.UserCreateResponseDtoMapper;
import com.stepanew.senlaproject.domain.mapper.user.UserUpdateMeResponseDtoMapper;
import com.stepanew.senlaproject.exceptions.AuthException;
import com.stepanew.senlaproject.exceptions.UserException;
import com.stepanew.senlaproject.repository.UserRepository;
import com.stepanew.senlaproject.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCreateRequestDtoMapper userCreateRequestDtoMapper;

    @Mock
    private UserUpdateMeResponseDtoMapper userUpdateMeResponseDtoMapper;

    @Mock
    private UserCreateResponseDtoMapper userCreateResponseDtoMapper;

    @Mock
    private CreateProfileRequestDtoMapper createProfileRequestDtoMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private static final Long DEFAULT_ID = 1L;

    private static final String DEFAULT_EMAIL = "test@example.com";

    private static final String DEFAULT_PASSWORD = "password";

    private static final String DEFAULT_FIRST_NAME = "Ivan";

    private static final String DEFAULT_LAST_NAME = "Ivanov";

    private static final String DEFAULT_PATRONYMIC = "Ivanovich";

    private static final String ENCODED_PASSWORD = "encodedPassword";

    @Test
    void getByIdTest() {
        User user = createUser();

        when(userRepository.findById(DEFAULT_ID))
                .thenReturn(Optional.of(user));

        User result = userService.getById(DEFAULT_ID);

        verify(userRepository).findById(DEFAULT_ID);
        Assertions.assertEquals(DEFAULT_ID, result.getId());
    }

    @Test
    void getByIdThrowsExceptionTest() {
        when(userRepository.findById(DEFAULT_ID))
                .thenReturn(Optional.empty());

        assertThrowsUserException(() -> userService.getById(DEFAULT_ID));

    }

    @Test
    void getByEmailTest() {
        User user = createUser();

        when(userRepository.findByEmail(DEFAULT_EMAIL))
                .thenReturn(Optional.of(user));

        User result = userService.getByEmail(DEFAULT_EMAIL);

        verify(userRepository).findByEmail(DEFAULT_EMAIL);
        Assertions.assertEquals(DEFAULT_EMAIL, result.getEmail());
    }

    @Test
    void getByEmailThrowsExceptionTest() {
        when(userRepository.findByEmail(DEFAULT_EMAIL))
                .thenReturn(Optional.empty());

        assertThrowsAuthException(() -> userService.getByEmail(DEFAULT_EMAIL));
    }

    @Test
    void createTest() {
        UserCreateRequestDto request = createUserCreateRequestDto();
        User user = createUser();
        UserCreatedResponseDto response = createUserCreatedResponseDto();

        when(userCreateRequestDtoMapper.toEntity(request))
                .thenReturn(user);
        when(userRepository.findByEmail(DEFAULT_EMAIL))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(DEFAULT_PASSWORD))
                .thenReturn(ENCODED_PASSWORD);
        when(userCreateResponseDtoMapper.toDto(user))
                .thenReturn(response);
        when(createProfileRequestDtoMapper.toEntity(request))
                .thenReturn(
                        new Profile(
                                DEFAULT_ID,
                                DEFAULT_FIRST_NAME,
                                DEFAULT_LAST_NAME,
                                DEFAULT_PATRONYMIC,
                                user)
                );

        UserCreatedResponseDto result = userService.create(request);

        verify(userRepository).save(user);
        Assertions.assertEquals(response, result);
    }

    @Test
    void createThrowsExceptionWhenEmailInUseTest() {
        UserCreateRequestDto request = createUserCreateRequestDto();
        User existingUser = createUser();

        when(userCreateRequestDtoMapper.toEntity(request))
                .thenReturn(existingUser);
        when(userRepository.findByEmail(DEFAULT_EMAIL))
                .thenReturn(Optional.of(existingUser));

        assertThrowsAuthException(() -> userService.create(request));
    }

    @Test
    void createThrowsExceptionWhenPasswordsDoNotMatchTest() {
        UserCreateRequestDto request = new UserCreateRequestDto(
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_PATRONYMIC,
                DEFAULT_PASSWORD,
                ENCODED_PASSWORD,
                DEFAULT_EMAIL
        );
        User user = createUser();

        when(userCreateRequestDtoMapper.toEntity(request))
                .thenReturn(user);

        assertThrowsAuthException(() -> userService.create(request));
    }

    @Test
    void updateMeTest() {
        UserUpdateMeRequestDto request = createUserUpdateMeRequestDto();
        User user = createUser();

        when(userRepository.findByEmail(DEFAULT_EMAIL))
                .thenReturn(Optional.of(user));
        when(userUpdateMeResponseDtoMapper.toDto(user))
                .thenReturn(createUserUpdateMeResponseDto());

        UserUpdateMeResponseDto result = userService.updateMe(request, DEFAULT_EMAIL);

        verify(userRepository).save(user);
        Assertions.assertEquals(DEFAULT_FIRST_NAME, result.firstName());
        Assertions.assertEquals(DEFAULT_LAST_NAME, result.lastName());
        Assertions.assertEquals(DEFAULT_PATRONYMIC, result.patronymic());
    }

    @Test
    void updateByIdTest() {
        UserUpdateMeRequestDto request = createUserUpdateMeRequestDto();
        User user = createUser();

        when(userRepository.findById(DEFAULT_ID))
                .thenReturn(Optional.of(user));
        when(userUpdateMeResponseDtoMapper.toDto(user))
                .thenReturn(createUserUpdateMeResponseDto());

        UserUpdateMeResponseDto result = userService.updateById(request, DEFAULT_ID);

        verify(userRepository).save(user);
        Assertions.assertEquals(DEFAULT_FIRST_NAME, result.firstName());
        Assertions.assertEquals(DEFAULT_LAST_NAME, result.lastName());
        Assertions.assertEquals(DEFAULT_PATRONYMIC, result.patronymic());
    }

    @Test
    void updateByIdThrowsExceptionWhenUserNotFoundTest() {
        UserUpdateMeRequestDto request = createUserUpdateMeRequestDto();

        when(userRepository.findById(DEFAULT_ID))
                .thenReturn(Optional.empty());

        assertThrowsUserException(() -> userService.updateById(request, DEFAULT_ID));
    }

    private UserUpdateMeResponseDto createUserUpdateMeResponseDto() {
        return new UserUpdateMeResponseDto(
                DEFAULT_EMAIL,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_PATRONYMIC
        );
    }

    private static UserUpdateMeRequestDto createUserUpdateMeRequestDto() {
        return new UserUpdateMeRequestDto(
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_PATRONYMIC
        );
    }

    private UserCreatedResponseDto createUserCreatedResponseDto() {
        return new UserCreatedResponseDto(
                DEFAULT_ID,
                DEFAULT_EMAIL,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_PATRONYMIC
        );
    }

    private UserCreateRequestDto createUserCreateRequestDto() {
        return new UserCreateRequestDto(
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_PATRONYMIC,
                DEFAULT_PASSWORD,
                DEFAULT_PASSWORD,
                DEFAULT_EMAIL
        );
    }

    private User createUser() {
        return new User(
                DEFAULT_ID,
                DEFAULT_PASSWORD,
                DEFAULT_EMAIL,
                null,
                null,
                new Profile()
        );
    }

    private void assertThrowsUserException(Runnable action) {
        Assertions.assertThrows(
                UserException.class, action::run
        );
    }

    private void assertThrowsAuthException(Runnable action) {
        Assertions.assertThrows(
                AuthException.class, action::run
        );
    }
}

package com.stepanew.senlaproject.security.service;

import com.stepanew.senlaproject.domain.entity.User;
import com.stepanew.senlaproject.exceptions.AuthException;
import com.stepanew.senlaproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByEmail(username)
                .orElseThrow(AuthException.CODE.NO_SUCH_EMAIL_OR_PASSWORD::get);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>(Collections.unmodifiableList(
                        user
                                .getRoles()
                                .stream()
                                .map(
                                        role -> new SimpleGrantedAuthority(role.getName())
                                )
                                .collect(Collectors.toList())
                ))
        );
    }

}

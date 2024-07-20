package com.stepanew.senlaproject.controller;

import com.stepanew.senlaproject.domain.dto.request.UserUpdateMeRequestDto;
import com.stepanew.senlaproject.domain.dto.response.UserUpdateMeResponseDto;
import com.stepanew.senlaproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<?> updateMe(
            Principal principal,
            @RequestBody @Validated UserUpdateMeRequestDto request
    ) {
        UserUpdateMeResponseDto response = userService.updateMe(request, principal.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateById(
            @PathVariable Long id,
            @RequestBody @Validated UserUpdateMeRequestDto request
    ) {
        UserUpdateMeResponseDto response = userService.updateById(request, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}

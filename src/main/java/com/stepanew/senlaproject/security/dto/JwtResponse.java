package com.stepanew.senlaproject.security.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {

    private String email;

    private String accessToken;

    private String refreshToken;

}

package com.stepanew.senlaproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stepanew.senlaproject.security.jwt.JwtCore;
import com.stepanew.senlaproject.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected JwtCore jwtCore;

    @MockBean
    protected UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    protected ObjectMapper objectMapper;

}

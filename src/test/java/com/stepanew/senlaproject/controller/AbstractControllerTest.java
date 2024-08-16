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

    protected static final String DEFAULT_NAME = "Name";

    protected static final String DEFAULT_DESCRIPTION = "Description";

    protected static final Long DEFAULT_ID = 1L;

    protected static final String CONTENT_TYPE = "application/json";

    protected static final String DEFAULT_FIRST_NAME = "Ivan";

    protected static final String DEFAULT_LAST_NAME = "Ivanov";

    protected static final String DEFAULT_PATRONYMIC = "Ivanovich";

    protected static final String DEFAULT_EMAIL = "test@example.com";

}

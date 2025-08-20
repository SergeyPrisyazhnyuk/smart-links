package ru.otus.authservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.authservice.AuthServiceApplication;
import ru.otus.authservice.model.RedirectResponse;
import ru.otus.authservice.model.Token;
import ru.otus.authservice.model.User;
import ru.otus.authservice.service.AuthService;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = {AuthServiceApplication.class})
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    ObjectMapper objectMapper = new ObjectMapper();

    Token token = new Token("sample-token");
    User user = new User(1L, "test-user", "test-password");
    List<User> users = new ArrayList<>();

    @BeforeEach
    void setUp() throws Exception {
        users.clear();
        users.add(new User(1L, "user1", "pass1"));
        users.add(new User(2L, "user2", "pass2"));
    }

    @Test
    void testLoginSuccess() throws Exception {
        when(authService.authenticate(any(), any())).thenReturn(true);
        RedirectResponse redirectResponse = new RedirectResponse("target-url", "token-value");
        when(authService.getRedirectInfo(any())).thenReturn(redirectResponse);

        this.mockMvc.perform(get("/auth/login").param("username", "test-user").param("password", "test-pass"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "target-url"))
                .andExpect(header().string("Authorization", "Bearer token-value"));
    }

    @Test
    void testLoginFailure() throws Exception {
        when(authService.authenticate(any(), any())).thenReturn(false);

        this.mockMvc.perform(get("/auth/login").param("username", "invalid-user").param("password", "wrong-pass"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAddUser() throws Exception {
        when(authService.addUser(any(User.class))).thenReturn(user);

        this.mockMvc.perform(post("/auth/save")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    void testGetUsers() throws Exception {
        when(authService.getUsers()).thenReturn(users);

        this.mockMvc.perform(get("/auth/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));
    }

    @Test
    void testGetUserById() throws Exception {
        when(authService.getUserById(any(Long.class))).thenReturn(user);

        this.mockMvc.perform(get("/auth/users/" + user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    void testDeleteUserById() throws Exception {
        this.mockMvc.perform(delete("/auth/users/" + user.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteAllUsers() throws Exception {
        this.mockMvc.perform(delete("/auth/users"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

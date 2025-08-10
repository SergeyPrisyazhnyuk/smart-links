package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.authservice.AuthServiceApplication;
import ru.otus.authservice.model.User;
import ru.otus.authservice.service.AuthService;
import ru.otus.authservice.token.JwtTokenProvider;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = AuthServiceApplication.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void successfulLoginTest() throws Exception {
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");

        when(authService.authenticate(any())).thenReturn(true);
        when(jwtTokenProvider.generateToken(eq("user"))).thenReturn("jwt-token");

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("{\"username\": \"user\", \"password\": \"password\"}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value("jwt-token"));
    }

    @Test
    public void failedLoginTest() throws Exception {
        User user = new User();
        user.setUsername("user");
        user.setPassword("wrong-password");

        when(authService.authenticate(any())).thenReturn(false);

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"username\": \"user\", \"password\": \"wrong-password\"}")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void addUserTest() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPass");

        when(authService.addUser(any(User.class)))
                .thenReturn(user);

        mockMvc.perform(post("/auth/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getUsersTest() throws Exception {

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword("pass1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setPassword("pass2");


        List<User> users = Arrays.asList(
                user1,
                user2
        );

        when(authService.getUsers()).thenReturn(users);

        mockMvc.perform(get("/auth/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[" +
                        "{\"id\":1,\"username\":\"user1\",\"password\":\"pass1\"}," +
                        "{\"id\":2,\"username\":\"user2\",\"password\":\"pass2\"}" +
                        "]"));
    }

    @Test
    public void getUserByIdTest() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword("pass1");

        when(authService.getUserById(1L)).thenReturn(user1);

        mockMvc.perform(get("/auth/users/" + 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"username\":\"user1\",\"password\":\"pass1\"}"));
    }

    @Test
    public void deleteUserByIdTest() throws Exception {
        long userId = 1L;
        doNothing().when(authService).deleteUserById(userId);

        mockMvc.perform(delete("/auth/users/" + userId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteAllUsersTest() throws Exception {
        doNothing().when(authService).deleteAllUsers();

        mockMvc.perform(delete("/auth/users"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
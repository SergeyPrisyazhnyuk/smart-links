package ru.otus.authservice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import ru.otus.authservice.model.RedirectResponse;
import ru.otus.authservice.model.User;
import ru.otus.authservice.repository.UserRepository;
import ru.otus.authservice.token.JwtTokenProvider;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest httpServletRequest;

    private User testUser;

    @BeforeEach
    void setup() {
        testUser = new User();
        testUser.setUsername("test_user");
        testUser.setPassword("test_password");
    }

    @Test
    void shouldAuthenticateValidUser() {
        given(userRepository.findByUsername(testUser.getUsername())).willReturn(testUser);
        assertThat(authService.authenticate(testUser.getUsername(), testUser.getPassword())).isTrue();
    }

    @Test
    void shouldNotAuthenticateInvalidUser() {
        given(userRepository.findByUsername(testUser.getUsername())).willReturn(null);
        assertThat(authService.authenticate(testUser.getUsername(), "incorrect_password")).isFalse();
    }

    @Test
    void shouldFailAuthenticationOnIncorrectPassword() {
        given(userRepository.findByUsername(testUser.getUsername())).willReturn(testUser);
        assertThat(authService.authenticate(testUser.getUsername(), "wrong_password")).isFalse();
    }


    @Test
    void shouldAddNewUserSuccessfully() {
        given(userRepository.findByUsername(testUser.getUsername())).willReturn(null);
        given(userRepository.save(testUser)).willReturn(testUser);
        assertThat(authService.addUser(testUser)).isEqualToComparingFieldByFieldRecursively(testUser);
    }

    @Test
    void shouldNotAddExistingUser() {
        given(userRepository.findByUsername(testUser.getUsername())).willReturn(testUser);
        assertThat(authService.addUser(testUser)).isNull();
    }

    @Test
    void shouldFetchAllUsers() {
        given(userRepository.findAll()).willReturn(Collections.singletonList(testUser));
        List<User> result = authService.getUsers();
        assertThat(result.size()).isOne();
        assertThat(result.get(0)).isEqualToComparingFieldByFieldRecursively(testUser);
    }

    @Test
    void shouldFindUserById() {
        given(userRepository.findById(1L)).willReturn(Optional.of(testUser));
        User foundUser = authService.getUserById(1L);
        assertThat(foundUser).isEqualToComparingFieldByFieldRecursively(testUser);
    }

    @Test
    void shouldReturnNullIfUserDoesntExist() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());
        assertThat(authService.getUserById(1L)).isNull();
    }

    @Test
    void shouldDeleteUserById() {
        doNothing().when(userRepository).deleteById(1L);
        authService.deleteUserById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldDeleteAllUsers() {
        doNothing().when(userRepository).deleteAll();
        authService.deleteAllUsers();
        verify(userRepository).deleteAll();
    }

    @Test
    void shouldHandleSuccessfulRedirectFlow() {
        given(httpServletRequest.getHeader("User-Agent")).willReturn("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        given(httpServletRequest.getHeader("Accept-Language")).willReturn("en-US,en;q=0.9");
        given(restTemplate.getForObject(any(), any())).willReturn("/dashboard");
        given(jwtTokenProvider.generateToken(testUser.getUsername())).willReturn("JWT_TOKEN");

        RedirectResponse response = authService.getRedirectInfo(testUser.getUsername());

        assertThat(response.getTargetUrl()).startsWith("http://localhost:8084/dashboard");
        assertThat(response.getToken()).isEqualTo("JWT_TOKEN");
    }

    @Test
    void shouldGracefullyHandleErrorInRestCall() {
        given(httpServletRequest.getHeader("User-Agent")).willReturn("Mozilla/5.0 (Linux; Android 10; SM-G973F Build/QP1A.190711.020; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/78.0.3904.108 Mobile Safari/537.36");
        given(httpServletRequest.getHeader("Accept-Language")).willReturn("ru-RU,ru;q=0.9");
        given(restTemplate.getForObject(any(), any())).willThrow(RuntimeException.class);

        RedirectResponse response = authService.getRedirectInfo(testUser.getUsername());
        assertThat(response).isNull();
    }
}
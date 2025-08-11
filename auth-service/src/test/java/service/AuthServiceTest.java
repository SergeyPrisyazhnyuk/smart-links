package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.authservice.model.User;
import ru.otus.authservice.repository.UserRepository;
import ru.otus.authservice.service.AuthService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticationSuccessTest() {
        User credentials = new User();
        credentials.setUsername("user");
        credentials.setPassword("password");

        User storedUser = new User();
        storedUser.setUsername("user");
        storedUser.setPassword("password");

        when(userRepository.findByUsername("user")).thenReturn(storedUser);

        assertTrue(authService.authenticate(credentials));
    }

    @Test
    void authenticationFailureTest() {
        User credentials = new User();
        credentials.setUsername("user");
        credentials.setPassword("incorrect");

        User storedUser = new User();
        storedUser.setUsername("user");
        storedUser.setPassword("correct");

        when(userRepository.findByUsername("user")).thenReturn(storedUser);

        assertFalse(authService.authenticate(credentials));
    }

    @Test
    void addNewUserTest() {
        User newUser = new User(null, "new_user", "password");

        given(userRepository.findByUsername(newUser.getUsername())).willReturn(null);
        given(userRepository.save(any(User.class))).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        assertThat(authService.addUser(newUser)).isEqualTo(newUser);
    }

    @Test
    void addExistingUserTest() {
        User existingUser = new User(1L, "existing_user", "password");

        given(userRepository.findByUsername(existingUser.getUsername())).willReturn(existingUser);

        assertThat(authService.addUser(existingUser)).isEqualTo(null);
    }

    @Test
    void getAllUsersTest() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "user1", "pass1"));
        users.add(new User(2L, "user2", "pass2"));

        given(userRepository.findAll()).willReturn(users);

        assertEquals(2, authService.getUsers().size());

    }

    @Test
    void getUserByValidIdTest() {
        User expectedUser = new User(1L, "user1", "pass1");

        given(userRepository.findById(expectedUser.getId())).willReturn(Optional.of(expectedUser));

        assertThat(authService.getUserById(expectedUser.getId())).isEqualTo(expectedUser);
    }

    @Test
    void getUserByInvalidIdTest() {
        given(userRepository.findById(-1L)).willReturn(Optional.empty());

        assertThat(authService.getUserById(-1L)).isNull();
    }

    @Test
    void deleteUserByIdTest() {
        long userId = 1L;

        doNothing().when(userRepository).deleteById(userId);

        authService.deleteUserById(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void deleteAllUsersTest() {
        doNothing().when(userRepository).deleteAll();

        authService.deleteAllUsers();

        verify(userRepository).deleteAll();
    }
}

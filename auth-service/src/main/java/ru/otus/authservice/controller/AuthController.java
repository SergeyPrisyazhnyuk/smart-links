package ru.otus.authservice.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.authservice.model.Token;
import ru.otus.authservice.model.User;
import ru.otus.authservice.service.AuthService;
import ru.otus.authservice.token.JwtTokenProvider;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    public String helloAuthService() {
        return "HI! This is auth-service!";
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody User credentials) {
        if (!authService.authenticate(credentials)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String token = jwtTokenProvider.generateToken(credentials.getUsername());
        return ResponseEntity.ok(new Token(token));
    }

    @PostMapping("/save")
    public User addUser(@RequestBody User user) {
        return authService.addUser(user);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return authService.getUsers();
    }

    @GetMapping("/users/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return authService.getUserById(userId);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        authService.deleteUserById(userId);
    }

    @DeleteMapping("/users")
    public void deleteUsers() {
        authService.deleteAllUsers();
    }
}
package ru.otus.authservice.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.authservice.model.RedirectResponse;
import ru.otus.authservice.model.Token;
import ru.otus.authservice.model.User;
import ru.otus.authservice.service.AuthService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping
    public String helloAuthService() {
        return "HI! This is auth-service!";
    }

    @GetMapping("/login")
    public ResponseEntity<Token> login(@RequestParam(name = "username") String login,
                                       @RequestParam(name = "password") String password) {

        if (!authService.authenticate(login, password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        RedirectResponse response = authService.getRedirectInfo(login);

        if (response != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", response.getTargetUrl());
            headers.add("Authorization", "Bearer " + response.getToken());
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
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
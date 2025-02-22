package com.biblioteca.biblioteca_spring.controller;

import com.biblioteca.biblioteca_spring.domain.user.CreateUserDto;
import com.biblioteca.biblioteca_spring.domain.user.User;
import com.biblioteca.biblioteca_spring.domain.user.UserRepository;
import com.biblioteca.biblioteca_spring.domain.user.UserResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid CreateUserDto data) {
        User savedUser = new User(data);

        this.userRepository.save(savedUser);

        UserResponseDto userResponseDto = new UserResponseDto(savedUser.getName(), savedUser.getEmail());

        return ResponseEntity.created(URI.create("/users/" + savedUser.getId())).body(userResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        List<UserResponseDto> userResponse = userRepository.findAllUserResponse();

        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) {
        User user = this.userRepository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserResponseDto userResponse = new UserResponseDto(user.getName(), user.getEmail());

        return ResponseEntity.ok().body(userResponse);
    }
}

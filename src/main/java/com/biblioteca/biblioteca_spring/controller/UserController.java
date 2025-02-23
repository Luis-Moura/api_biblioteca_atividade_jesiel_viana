package com.biblioteca.biblioteca_spring.controller;

import com.biblioteca.biblioteca_spring.domain.user.User;
import com.biblioteca.biblioteca_spring.domain.user.UserRepository;
import com.biblioteca.biblioteca_spring.domain.user.dto.CreateUserDto;
import com.biblioteca.biblioteca_spring.domain.user.dto.UpdateUserDto;
import com.biblioteca.biblioteca_spring.domain.user.dto.UserResponseDto;
import com.biblioteca.biblioteca_spring.infra.exception.BadRequestException;
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

        UserResponseDto userResponse = new UserResponseDto(savedUser.getName(), savedUser.getEmail());

        return ResponseEntity.created(URI.create("/users/" + savedUser.getId())).body(userResponse);
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
            throw new BadRequestException("User Not Found");
        }

        UserResponseDto userResponse = new UserResponseDto(user.getName(), user.getEmail());

        return ResponseEntity.ok().body(userResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUserDto(@PathVariable UUID id, @RequestBody @Valid UpdateUserDto data) {
        User user = this.userRepository.findById(id).orElse(null);

        if (user == null) {
            throw new BadRequestException("User Not Found");
        }

        if (data.name() == null && data.email() == null) {
            throw new BadRequestException("Name AND Email Cannot be Empty");
        }

        if (data.name() != null) {
            user.setName(data.name());
        }

        if (data.email() != null) {
            user.setEmail(data.email());
        }

        this.userRepository.save(user);

        UserResponseDto userResponse = new UserResponseDto(user.getName(), user.getEmail());

        return ResponseEntity.ok().body(userResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        User user = this.userRepository.findById(id).orElse(null);

        if (user == null) {
            throw new BadRequestException("User Not Found");
        }

        this.userRepository.delete(user);

        return ResponseEntity.ok("Successful Deleted User!");
    }
}

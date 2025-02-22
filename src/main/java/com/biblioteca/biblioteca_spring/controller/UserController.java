package com.biblioteca.biblioteca_spring.controller;

import com.biblioteca.biblioteca_spring.domain.user.CreateUserDto;
import com.biblioteca.biblioteca_spring.domain.user.User;
import com.biblioteca.biblioteca_spring.domain.user.UserRepository;
import com.biblioteca.biblioteca_spring.domain.user.UserResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

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
}

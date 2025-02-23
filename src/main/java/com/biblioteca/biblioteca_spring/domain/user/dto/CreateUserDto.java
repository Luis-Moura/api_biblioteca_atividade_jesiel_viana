package com.biblioteca.biblioteca_spring.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDto(@NotBlank String name, @NotBlank @Email String email) {
}

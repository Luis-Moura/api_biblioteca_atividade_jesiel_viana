package com.biblioteca.biblioteca_spring.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateUserDto(@NotBlank String name, @NotBlank String email) {
}

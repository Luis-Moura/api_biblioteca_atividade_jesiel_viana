package com.biblioteca.biblioteca_spring.domain.user;

import jakarta.validation.constraints.NotBlank;

public record UserResponseDto(@NotBlank String name, @NotBlank String email) {
}

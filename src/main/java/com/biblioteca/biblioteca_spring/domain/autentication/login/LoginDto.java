package com.biblioteca.biblioteca_spring.domain.autentication.login;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(@NotBlank String email, @NotBlank String password) {
}

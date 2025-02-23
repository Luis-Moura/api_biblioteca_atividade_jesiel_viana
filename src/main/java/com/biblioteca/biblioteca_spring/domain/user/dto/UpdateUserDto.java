package com.biblioteca.biblioteca_spring.domain.user.dto;

import jakarta.validation.constraints.Email;

public record UpdateUserDto(String name, @Email String email) {
}

package com.biblioteca.biblioteca_spring.domain.autentication.login;

public record LoginResponseDto(String accessToken, Long expiresIn) {
}

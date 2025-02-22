package com.biblioteca.biblioteca_spring.domain.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private String name;
    private String email;

    public UserResponseDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}

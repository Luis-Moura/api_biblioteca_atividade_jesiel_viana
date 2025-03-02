package com.biblioteca.biblioteca_spring.domain.user;

import com.biblioteca.biblioteca_spring.domain.autentication.register.RegisterUserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRoles userRoles;

    public User(RegisterUserDto registerUserDto) {
        this.name = registerUserDto.name();
        this.email = registerUserDto.email();
    }
}

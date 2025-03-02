package com.biblioteca.biblioteca_spring.domain.user;

import com.biblioteca.biblioteca_spring.domain.user.dto.UserResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT new com.biblioteca.biblioteca_spring.domain.user.dto.UserResponseDto(u.name, u.email) FROM User u")
    List<UserResponseDto> findAllUserResponse();

    Optional<User> findByEmail(String email);
}

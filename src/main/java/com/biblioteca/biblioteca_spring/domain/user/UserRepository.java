package com.biblioteca.biblioteca_spring.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT new com.biblioteca.biblioteca_spring.domain.user.UserResponseDto(u.name, u.email) FROM User u")
    List<UserResponseDto> findAllUserResponse();
}

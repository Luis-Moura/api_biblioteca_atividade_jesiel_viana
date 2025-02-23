package com.biblioteca.biblioteca_spring.domain.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    List<Book> findByAmountGreaterThan(Integer amount);
}

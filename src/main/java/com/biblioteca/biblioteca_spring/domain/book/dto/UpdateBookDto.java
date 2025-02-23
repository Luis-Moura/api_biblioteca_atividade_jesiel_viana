package com.biblioteca.biblioteca_spring.domain.book.dto;

public record UpdateBookDto(
        String title,
        String author,
        String publisher,
        Integer year,
        Integer amount
) {
}

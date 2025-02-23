package com.biblioteca.biblioteca_spring.domain.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBookDto(
        @NotBlank String title,
        @NotBlank String author,
        @NotBlank String publisher,
        @NotNull Integer year,
        @NotNull Integer amount
) {
}

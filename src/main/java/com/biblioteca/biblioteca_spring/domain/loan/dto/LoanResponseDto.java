package com.biblioteca.biblioteca_spring.domain.loan.dto;

import com.biblioteca.biblioteca_spring.domain.book.Book;

import java.time.LocalDate;
import java.util.UUID;

public record LoanResponseDto(LocalDate loanDate, LocalDate returnDate, UUID userId, Book book) {
}

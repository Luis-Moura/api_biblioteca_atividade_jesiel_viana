package com.biblioteca.biblioteca_spring.domain.book;

import com.biblioteca.biblioteca_spring.domain.book.dto.CreateBookDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 150)
    private String author;

    @Column(nullable = false, length = 150)
    private String publisher;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer amount;

    public Book(CreateBookDto createBookDto) {
        this.title = createBookDto.title();
        this.author = createBookDto.author();
        this.publisher = createBookDto.publisher();
        this.year = createBookDto.year();
        this.amount = createBookDto.amount();
    }
}

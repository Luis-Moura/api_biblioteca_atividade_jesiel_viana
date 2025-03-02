package com.biblioteca.biblioteca_spring.controller;

import com.biblioteca.biblioteca_spring.domain.book.Book;
import com.biblioteca.biblioteca_spring.domain.book.BookRepository;
import com.biblioteca.biblioteca_spring.domain.book.dto.CreateBookDto;
import com.biblioteca.biblioteca_spring.domain.book.dto.UpdateBookDto;
import com.biblioteca.biblioteca_spring.infra.exception.BadRequestException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @PreAuthorize("hasAnyAuthority('SCOPE_LIBRARIAN', 'SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody @Valid CreateBookDto data) {
        Book savedBook = new Book(data);

        this.bookRepository.save(savedBook);

        return ResponseEntity.created(URI.create("/books/" + savedBook.getId())).body(savedBook);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        List<Book> books = this.bookRepository.findAll();

        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Book>> getAvailableBooks() {
        List<Book> availableBooks = this.bookRepository.findByAmountGreaterThan(0);

        return ResponseEntity.ok().body(availableBooks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable UUID id) {
        Book book = this.bookRepository.findById(id).orElse(null);

        if (book == null) {
            throw new BadRequestException("Book Not Found!");
        }

        return ResponseEntity.ok().body(book);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_LIBRARIAN', 'SCOPE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable UUID id, @RequestBody @Valid UpdateBookDto data) {
        Book book = this.bookRepository.findById(id).orElse(null);

        if (book == null) {
            throw new BadRequestException("Book Not Found!");
        }

        if (data.title() == null && data.author() == null && data.publisher() == null && data.year() == null && data.amount() == null) {
            throw new BadRequestException("Title AND Autor AND Publisher AND Year AND Amount Cannot be Empty");
        }

        if (data.title() != null) {
            book.setTitle(data.title());
        }

        if (data.author() != null) {
            book.setAuthor(data.author());
        }

        if (data.publisher() != null) {
            book.setPublisher(data.publisher());
        }

        if (data.year() != null) {
            book.setYear(data.year());
        }

        if (data.amount() != null) {
            book.setAmount(data.amount());
        }

        this.bookRepository.save(book);

        return ResponseEntity.ok().body(book);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_LIBRARIAN', 'SCOPE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable UUID id) {
        Book book = this.bookRepository.findById(id).orElse(null);

        if (book == null) {
            throw new BadRequestException("Book Not Found!");
        }

        this.bookRepository.delete(book);

        return ResponseEntity.ok().body("Successful Deleted Book!");
    }
}

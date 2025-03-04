package com.biblioteca.biblioteca_spring.controller;

import com.biblioteca.biblioteca_spring.domain.book.Book;
import com.biblioteca.biblioteca_spring.domain.book.BookRepository;
import com.biblioteca.biblioteca_spring.domain.loan.Loan;
import com.biblioteca.biblioteca_spring.domain.loan.LoanRepository;
import com.biblioteca.biblioteca_spring.domain.loan.dto.LoanResponseDto;
import com.biblioteca.biblioteca_spring.domain.user.User;
import com.biblioteca.biblioteca_spring.domain.user.UserRepository;
import com.biblioteca.biblioteca_spring.infra.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/loans")
public class LoanController {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/{bookId}")
    public ResponseEntity<LoanResponseDto> createLoan(JwtAuthenticationToken token, @PathVariable UUID bookId) {
        User user = this.userRepository.findById(UUID.fromString(token.getName())).orElse(null);

        if (user == null) {
            throw new BadRequestException("User Not Found.");
        }

        Book book = this.bookRepository.findById(bookId).orElse(null);

        if (book == null) {
            throw new BadRequestException("Book Not Found.");
        }

        if (book.getAmount() <= 0) {
            throw new BadRequestException("There Are no Available Books");
        }

        Loan savedLoan = new Loan();
        savedLoan.setUser(user);
        savedLoan.setBook(book);
        savedLoan.setLoanDate(LocalDate.now());
        savedLoan.setReturnDate(LocalDate.now().plusDays(15));
        savedLoan.setIsReturned(false);
        this.loanRepository.save(savedLoan);

        book.setAmount(book.getAmount() - 1);
        this.bookRepository.save(book);

        LoanResponseDto loanResponse = new LoanResponseDto(
                savedLoan.getLoanDate(),
                savedLoan.getReturnDate(),
                savedLoan.getUser().getId(),
                savedLoan.getBook()
        );

        return ResponseEntity.created(URI.create("/loans/" + savedLoan.getId())).body(loanResponse);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_LIBRARIAN', 'SCOPE_ADMIN')")
    public ResponseEntity<List<LoanResponseDto>> getLoans() {
        List<LoanResponseDto> loanResponseList = this.loanRepository.findAllLoanResponse();

        return ResponseEntity.ok().body(loanResponseList);
    }

    @GetMapping("/user")
    public ResponseEntity<List<LoanResponseDto>> getUserLoans(JwtAuthenticationToken token) {
        User user = this.userRepository.findById(UUID.fromString(token.getName())).orElse(null);

        if (user == null) {
            throw new BadRequestException("User Not Found.");
        }

        List<LoanResponseDto> loanResponseList = this.loanRepository.findLoansByUserId(user.getId());

        return ResponseEntity.ok().body(loanResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponseDto> getLoanById(@PathVariable UUID id, JwtAuthenticationToken token) {
        User user = this.userRepository.findById(UUID.fromString(token.getName())).orElse(null);

        if (user == null) {
            throw new BadRequestException("User Not Found.");
        }

        Loan loan = this.loanRepository.findByIdAndUserId(id, user.getId()).orElse(null);

        if (loan == null) {
            throw new BadRequestException("Loan Bot Found");
        }

        LoanResponseDto loanResponse = new LoanResponseDto(
                loan.getLoanDate(),
                loan.getReturnDate(),
                loan.getUser().getId(),
                loan.getBook()
        );

        return ResponseEntity.ok().body(loanResponse);
    }

    @PatchMapping("/return/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIBRARIAN', 'SCOPE_ADMIN')")
    public ResponseEntity<LoanResponseDto> returnLoan(@PathVariable UUID id) {
        Loan loan = this.loanRepository.findById(id).orElse(null);

        if (loan == null) {
            throw new BadRequestException("Loan Not Found");
        }

        if (loan.getIsReturned()) {
            throw new BadRequestException("Book has Already Been Returned");
        }

        loan.setIsReturned(true);

        Book book = this.bookRepository.findById(loan.getBook().getId()).orElse(null);

        if (book == null) {
            throw new BadRequestException("Book Not Found");
        }

        book.setAmount(book.getAmount() + 1);

        this.loanRepository.save(loan);
        this.bookRepository.save(book);

        LoanResponseDto loanResponse = new LoanResponseDto(
                loan.getLoanDate(),
                loan.getReturnDate(),
                loan.getUser().getId(),
                loan.getBook()
        );

        return ResponseEntity.ok().body(loanResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIBRARIAN', 'SCOPE_ADMIN')")
    public ResponseEntity<String> deleteLoan(@PathVariable UUID id) {
        Loan loan = this.loanRepository.findById(id).orElse(null);

        if (loan == null) {
            throw new BadRequestException("Loan Not Found");
        }

        if (!loan.getIsReturned()) {
            throw new BadRequestException("Loan is Not Returned Yet");
        }

        this.loanRepository.delete(loan);

        return ResponseEntity.ok().body("Successful Deleted Loan");
    }
}

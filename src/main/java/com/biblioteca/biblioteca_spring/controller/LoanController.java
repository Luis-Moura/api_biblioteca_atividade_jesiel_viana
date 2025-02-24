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
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
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

    @PostMapping("/{userId}/{bookId}")
    public ResponseEntity<LoanResponseDto> createLoan(@PathVariable UUID userId, @PathVariable UUID bookId) {
        User user = this.userRepository.findById(userId).orElse(null);

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
    public ResponseEntity<List<LoanResponseDto>> getLoans() {
        List<LoanResponseDto> loanResponseList = this.loanRepository.findAllLoanResponse();

        return ResponseEntity.ok().body(loanResponseList);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanResponseDto>> getUserLoans(@PathVariable UUID userId) {
        List<LoanResponseDto> loanResponseList = this.loanRepository.findLoansByUserId(userId);

        return ResponseEntity.ok().body(loanResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponseDto> getLoanById(@PathVariable UUID id) {
        Loan loan = this.loanRepository.findById(id).orElse(null);

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

    @PatchMapping("/{id}")
    public ResponseEntity<LoanResponseDto> returnLoan(@PathVariable UUID id) {
        Loan loan = this.loanRepository.findById(id).orElse(null);

        if (loan == null) {
            throw new BadRequestException("Loan Bot Found");
        }

        if (loan.getIsReturned()) {
            throw new BadRequestException("Book has Already Been Returned");
        }

        loan.setIsReturned(true);
        this.loanRepository.save(loan);

        Book book = this.bookRepository.findById(loan.getBook().getId()).orElse(null);

        if (book == null) {
            throw new BadRequestException("Book Not Found");
        }

        book.setAmount(book.getAmount() + 1);
        this.bookRepository.save(book);

        LoanResponseDto loanResponse = new LoanResponseDto(
                loan.getLoanDate(),
                loan.getReturnDate(),
                loan.getUser().getId(),
                loan.getBook()
        );

        return ResponseEntity.ok().body(loanResponse);
    }
}

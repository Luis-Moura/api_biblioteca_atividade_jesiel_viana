package com.biblioteca.biblioteca_spring.domain.loan;

import com.biblioteca.biblioteca_spring.domain.loan.dto.LoanResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LoanRepository extends JpaRepository<Loan, UUID> {
    @Query("SELECT new com.biblioteca.biblioteca_spring.domain.loan.dto.LoanResponseDto(l.loanDate, l.returnDate, l.user.id, l.book) FROM Loan l")
    List<LoanResponseDto> findAllLoanResponse();

    @Query("SELECT new com.biblioteca.biblioteca_spring.domain.loan.dto.LoanResponseDto(l.loanDate, l.returnDate, l.user.id, l.book) FROM Loan l WHERE l.user.id = :userId")
    List<LoanResponseDto> findLoansByUserId(@Param("userId") UUID userId);
}

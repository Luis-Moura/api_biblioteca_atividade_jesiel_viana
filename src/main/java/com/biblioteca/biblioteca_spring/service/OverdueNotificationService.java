package com.biblioteca.biblioteca_spring.service;

import com.biblioteca.biblioteca_spring.domain.loan.Loan;
import com.biblioteca.biblioteca_spring.domain.loan.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class OverdueNotificationService {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private NotificationService notificationService;

    @Scheduled(cron = "0 0 8 * * *") // Executa todos os dias às 8:00
    public void checkOverdueLoans() {
        LocalDate today = LocalDate.now();

        List<Loan> allLoans = loanRepository.findAll();

        for (Loan loan : allLoans) {
            if (!loan.getIsReturned() && loan.getReturnDate().isBefore(today)) {
                long daysOverdue = ChronoUnit.DAYS.between(loan.getReturnDate(), today);

                notificationService.sendOverdueNotification(
                        loan.getUser(),
                        loan.getBook(),
                        loan,
                        daysOverdue
                );
            }
        }
    }
}
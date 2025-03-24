package com.biblioteca.biblioteca_spring.service;

import com.biblioteca.biblioteca_spring.domain.book.Book;
import com.biblioteca.biblioteca_spring.domain.loan.Loan;
import com.biblioteca.biblioteca_spring.domain.user.User;

public interface NotificationService {
    boolean sendWelcomeNotification(User user);

    boolean sendLoanNotification(User user, Book book, Loan loan);

    boolean sendOverdueNotification(User user, Book book, Loan loan, long daysOverdue);
}
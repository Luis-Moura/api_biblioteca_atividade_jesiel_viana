package com.biblioteca.biblioteca_spring.service;

import com.biblioteca.biblioteca_spring.domain.book.Book;
import com.biblioteca.biblioteca_spring.domain.loan.Loan;
import com.biblioteca.biblioteca_spring.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailNotificationService implements NotificationService {
    @Autowired
    private JavaMailSender mailSender;

    private static final String SENDER_EMAIL = "biblioteca@exemplo.com";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public boolean sendWelcomeNotification(User user) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(SENDER_EMAIL);
            message.setTo(user.getEmail());
            message.setSubject("Bem-vindo à Biblioteca!");
            message.setText(
                    "Olá, " + user.getName() + "!\n\n" +
                            "Seja bem-vindo à nossa Biblioteca. Seu cadastro foi realizado com sucesso.\n\n" +
                            "Agora você pode acessar nosso sistema e realizar empréstimos de livros.\n\n" +
                            "Atenciosamente,\n" +
                            "Equipe da Biblioteca"
            );

            mailSender.send(message);
            return true;
        } catch (MailException e) {
            return false;
        }
    }

    @Override
    public boolean sendLoanNotification(User user, Book book, Loan loan) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(SENDER_EMAIL);
            message.setTo(user.getEmail());
            message.setSubject("Confirmação de Empréstimo - " + book.getTitle());
            message.setText(
                    "Olá, " + user.getName() + "!\n\n" +
                            "Seu empréstimo foi realizado com sucesso.\n\n" +
                            "Detalhes do livro:\n" +
                            "Título: " + book.getTitle() + "\n" +
                            "Autor: " + book.getAuthor() + "\n" +
                            "Editora: " + book.getPublisher() + "\n\n" +
                            "Data de empréstimo: " + loan.getLoanDate().format(DATE_FORMATTER) + "\n" +
                            "Data de devolução: " + loan.getReturnDate().format(DATE_FORMATTER) + "\n\n" +
                            "Por favor, devolva o livro até a data indicada para evitar penalidades.\n\n" +
                            "Atenciosamente,\n" +
                            "Equipe da Biblioteca"
            );

            mailSender.send(message);
            return true;
        } catch (MailException e) {
            return false;
        }
    }

    @Override
    public boolean sendOverdueNotification(User user, Book book, Loan loan, long daysOverdue) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(SENDER_EMAIL);
            message.setTo(user.getEmail());
            message.setSubject("URGENTE: Livro com devolução em atraso - " + book.getTitle());
            message.setText(
                    "Olá, " + user.getName() + "!\n\n" +
                            "O livro \"" + book.getTitle() + "\" está com " + daysOverdue +
                            (daysOverdue == 1 ? " dia" : " dias") + " de atraso na devolução.\n\n" +
                            "Data de empréstimo: " + loan.getLoanDate().format(DATE_FORMATTER) + "\n" +
                            "Data prevista para devolução: " + loan.getReturnDate().format(DATE_FORMATTER) + "\n\n" +
                            "Por favor, devolva o livro o mais rápido possível para evitar penalidades adicionais.\n\n" +
                            "Atenciosamente,\n" +
                            "Equipe da Biblioteca"
            );

            mailSender.send(message);
            return true;
        } catch (MailException e) {
            return false;
        }
    }
}
package com.biblioteca.biblioteca_spring.service;

import com.biblioteca.biblioteca_spring.domain.book.Book;
import com.biblioteca.biblioteca_spring.domain.loan.Loan;
import com.biblioteca.biblioteca_spring.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
public class EmailNotificationService implements NotificationService {
    @Autowired
    private JavaMailSender mailSender;

    private static final String SENDER_EMAIL = "biblioteca@exemplo.com";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    @Async("taskExecutor")
    public void sendWelcomeNotification(User user) {
        String messageId = UUID.randomUUID().toString();
        log.info("Iniciando envio de e-mail de boas-vindas [ID: {}] para {}", messageId, user.getEmail());

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

        try {
            mailSender.send(message);
            log.info("✅ E-mail de boas-vindas [ID: {}] enviado com sucesso para {}", messageId, user.getEmail());
        } catch (MailException e) {
            log.error("❌ Erro ao enviar e-mail de boas-vindas [ID: {}] para {}: {}", messageId, user.getEmail(), e.getMessage());
            log.debug("Detalhes do erro: ", e);
        }
    }

    @Override
    @Async("taskExecutor")
    public void sendLoanNotification(User user, Book book, Loan loan) {
        String messageId = UUID.randomUUID().toString();
        log.info("Iniciando envio de e-mail de empréstimo [ID: {}] para {} - Livro: {}",
                messageId, user.getEmail(), book.getTitle());

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

        try {
            mailSender.send(message);
            log.info("✅ E-mail de empréstimo [ID: {}] enviado com sucesso para {} - Livro: {}",
                    messageId, user.getEmail(), book.getTitle());
        } catch (MailException e) {
            log.error("❌ Erro ao enviar e-mail de empréstimo [ID: {}] para {} - Livro: {}: {}",
                    messageId, user.getEmail(), book.getTitle(), e.getMessage());
            log.debug("Detalhes do erro: ", e);
        }
    }

    @Override
    @Async("taskExecutor")
    public void sendOverdueNotification(User user, Book book, Loan loan, long daysOverdue) {
        String messageId = UUID.randomUUID().toString();
        log.info("Iniciando envio de e-mail de atraso [ID: {}] para {} - Livro: {} - Atraso: {} dias",
                messageId, user.getEmail(), book.getTitle(), daysOverdue);

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

        try {
            mailSender.send(message);
            log.info("✅ E-mail de atraso [ID: {}] enviado com sucesso para {} - Livro: {} - Atraso: {} dias",
                    messageId, user.getEmail(), book.getTitle(), daysOverdue);
        } catch (MailException e) {
            log.error("❌ Erro ao enviar e-mail de atraso [ID: {}] para {} - Livro: {} - Atraso: {} dias: {}",
                    messageId, user.getEmail(), book.getTitle(), daysOverdue, e.getMessage());
            log.debug("Detalhes do erro: ", e);
        }
    }
}
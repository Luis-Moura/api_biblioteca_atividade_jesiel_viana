package com.biblioteca.biblioteca_spring;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliotecaSpringApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
//        System.setProperty("PRIVATE_KEY", dotenv.get("PRIVATE_KEY"));
//        System.setProperty("PUBLIC_KEY", dotenv.get("PUBLIC_KEY"));

        SpringApplication.run(BibliotecaSpringApplication.class, args);
    }

}

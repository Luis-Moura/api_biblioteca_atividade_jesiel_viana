package com.biblioteca.biblioteca_spring.config;

import com.biblioteca.biblioteca_spring.domain.user.User;
import com.biblioteca.biblioteca_spring.domain.user.UserRepository;
import com.biblioteca.biblioteca_spring.domain.user.UserRoles;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AdminUserConfig {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @PostConstruct
    public void run() {
        var userAdmin = this.userRepository.findByEmail("admin");

        userAdmin.ifPresentOrElse(
                (user) -> {
                    System.out.println("Admin Already Exists");
                },
                () -> {
                    var user = new User();
                    user.setName("admin");
                    user.setEmail("admin");
                    user.setPassword(bCryptPasswordEncoder.encode(adminPassword));
                    user.setUserRoles(UserRoles.ADMIN);
                    userRepository.save(user);
                }
        );
    }
}

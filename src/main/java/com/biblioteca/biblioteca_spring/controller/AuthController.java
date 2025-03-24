package com.biblioteca.biblioteca_spring.controller;

import com.biblioteca.biblioteca_spring.domain.autentication.login.LoginDto;
import com.biblioteca.biblioteca_spring.domain.autentication.login.LoginResponseDto;
import com.biblioteca.biblioteca_spring.domain.autentication.register.RegisterUserDto;
import com.biblioteca.biblioteca_spring.domain.user.User;
import com.biblioteca.biblioteca_spring.domain.user.UserRepository;
import com.biblioteca.biblioteca_spring.domain.user.UserRoles;
import com.biblioteca.biblioteca_spring.domain.user.dto.UserResponseDto;
import com.biblioteca.biblioteca_spring.infra.exception.BadRequestException;
import com.biblioteca.biblioteca_spring.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.Instant;
import java.util.Optional;

@RestController()
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtEncoder jwtEncoder;

    public AuthController(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid RegisterUserDto data) {
        Optional<User> existingUser = this.userRepository.findByEmail(data.email());

        if (existingUser.isPresent()) {
            throw new BadRequestException("User Already Exists");
        }

        User savedUser = new User();
        savedUser.setName(data.name());
        savedUser.setEmail(data.email());
        savedUser.setPassword(bCryptPasswordEncoder.encode(data.password()));
        savedUser.setUserRoles(UserRoles.USER);

        this.userRepository.save(savedUser);

        notificationService.sendWelcomeNotification(savedUser);

        UserResponseDto userResponse = new UserResponseDto(savedUser.getName(), savedUser.getEmail());

        return ResponseEntity.created(URI.create("/users/" + savedUser.getId())).body(userResponse);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping("/register-librarian")
    public ResponseEntity<UserResponseDto> registerLibrarian(@RequestBody @Valid RegisterUserDto data) {
        Optional<User> existingUser = this.userRepository.findByEmail(data.email());

        if (existingUser.isPresent()) {
            throw new BadRequestException("User Already Exists");
        }

        User savedUser = new User();
        savedUser.setName(data.name());
        savedUser.setEmail(data.email());
        savedUser.setPassword(bCryptPasswordEncoder.encode(data.password()));
        savedUser.setUserRoles(UserRoles.LIBRARIAN);

        this.userRepository.save(savedUser);

        UserResponseDto userResponse = new UserResponseDto(savedUser.getName(), savedUser.getEmail());

        return ResponseEntity.created(URI.create("/users/" + savedUser.getId())).body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginDto loginDto) {
        Optional<User> userOptional = userRepository.findByEmail(loginDto.email());

        if (userOptional.isEmpty()) {
            throw new BadCredentialsException("User or Password is Invalid");
        }

        User user = userOptional.get();

        boolean validadePassword = bCryptPasswordEncoder.matches(loginDto.password(), user.getPassword());

        if (!validadePassword) {
            throw new BadCredentialsException("User or Password is Invalid");
        }

        var now = Instant.now();
        var expiresIn = 3600L;

        var scope = user.getUserRoles();

        var claims = JwtClaimsSet.builder()
                .issuer("biblioteca")
                .subject(user.getId().toString())
                .expiresAt(now.plusSeconds(expiresIn))
                .issuedAt(now)
                .claim("scope", scope)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponseDto(jwtValue, expiresIn));
    }
}

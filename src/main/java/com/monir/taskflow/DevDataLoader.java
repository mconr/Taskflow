package com.monir.taskflow;

import com.monir.taskflow.model.Role;
import com.monir.taskflow.model.User;
import com.monir.taskflow.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DevDataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DevDataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Create a default dev user if missing (useful with H2 in-memory DB)
        String username = "diag_user_live";
        String rawPassword = "DiagP@ss123";
        userRepository.findByUsername(username).orElseGet(() ->
            userRepository.save(User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .role(Role.USER)
                .build())
        );
    }
}

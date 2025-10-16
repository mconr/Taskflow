package com.monir.taskflow.controller;


import com.monir.taskflow.model.Role;
import com.monir.taskflow.model.User;
import com.monir.taskflow.repository.UserRepository;
import com.monir.taskflow.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
private final UserRepository userRepo;
private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
private final AuthenticationManager authManager;
private final JwtUtil jwtUtil;
private final Logger logger = LoggerFactory.getLogger(AuthController.class);


public AuthController(UserRepository userRepo, AuthenticationManager authManager, JwtUtil jwtUtil) {
this.userRepo = userRepo;
this.authManager = authManager;
this.jwtUtil = jwtUtil;
}

// Temporary debug endpoint - remove before production
@GetMapping("/debug-users")
public ResponseEntity<?> debugUsers() {
	return ResponseEntity.ok(userRepo.findAll().stream().map(u -> Map.of("username", u.getUsername(), "role", u.getRole())).toList());
}


@PostMapping("/register")
public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
String username = body.get("username");
String raw = body.get("password");
if (userRepo.findByUsername(username).isPresent()) return ResponseEntity.badRequest().body(Map.of("error", "username exists"));
User u = User.builder().username(username).password(passwordEncoder.encode(raw)).role(Role.USER).build();
userRepo.save(u);
return ResponseEntity.ok(Map.of("msg", "registered"));
}


@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
String username = body.get("username");
String password = body.get("password");
try {
authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
String token = jwtUtil.generateToken(username);
return ResponseEntity.ok(Map.of("token", token));
} catch (BadCredentialsException ex) {
	return ResponseEntity.status(401).body(Map.of("error", "invalid credentials"));
} catch (Exception ex) {
	// Log server-side; return a generic error message to avoid leaking internal details
	logger.error("Unexpected error during login for user {}", username, ex);
	return ResponseEntity.status(500).body(Map.of("error", "internal_error"));
}
}
}
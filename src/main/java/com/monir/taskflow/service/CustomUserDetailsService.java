package com.monir.taskflow.service;


import com.monir.taskflow.model.User;
import com.monir.taskflow.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Collections;


@Service
public class CustomUserDetailsService implements UserDetailsService {
private final UserRepository userRepo;


public CustomUserDetailsService(UserRepository userRepo) { this.userRepo = userRepo; }


@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
User user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
return new org.springframework.security.core.userdetails.User(
user.getUsername(), user.getPassword(),
Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
}
}
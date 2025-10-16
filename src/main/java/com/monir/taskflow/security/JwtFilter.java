package com.monir.taskflow.security;

import com.monir.taskflow.service.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import java.io.IOException;


@Component
public class JwtFilter extends OncePerRequestFilter {
	private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService userDetailsService;

	public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			if (jwtUtil.validateToken(token)) {
				String username = jwtUtil.getUsernameFromToken(token);
				try {
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					if (userDetails != null) {
						UsernamePasswordAuthenticationToken auth =
								new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
						SecurityContextHolder.getContext().setAuthentication(auth);
					}
				} catch (UsernameNotFoundException ex) {
					// User referred by token no longer exists. Log and continue without setting authentication.
					logger.warn("User from JWT not found: {}", username);
				} catch (Exception ex) {
					// Defensive: do not let unexpected exceptions break the filter chain and return 500.
					logger.error("Unexpected error while setting authentication from JWT", ex);
				}
			}
		}
		filterChain.doFilter(request, response);
	}
}
package com.eric.ecommerce.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eric.ecommerce.dto.auth.LoginRequest;
import com.eric.ecommerce.dto.auth.LoginResponse;
import com.eric.ecommerce.dto.auth.RegisterRequest;
import com.eric.ecommerce.exceptions.EmailAlreadyExistsException;
import com.eric.ecommerce.exceptions.InvalidCredentialsException;
import com.eric.ecommerce.model.AppUser;
import com.eric.ecommerce.model.Role;
import com.eric.ecommerce.repository.UserRepository;

@Service
public class AuthService {

	private final CustomUserDetailsService customUserDetailsService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.customUserDetailsService = customUserDetailsService;
	}

	public void register(RegisterRequest request) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new EmailAlreadyExistsException("E-mail já cadastrado");
		}

		AppUser user = new AppUser();

		user.setNome(request.getNome());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(Role.USER);
		userRepository.save(user);

	}

	public LoginResponse login(LoginRequest request) {

		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
			return new LoginResponse("Login realizado com sucesso");

		} catch (BadCredentialsException ex) {
			throw new InvalidCredentialsException("Credenciais inválidas");
		}

	}

}

package com.eric.ecommerce.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eric.ecommerce.dto.auth.RegisterRequest;
import com.eric.ecommerce.model.AppUser;
import com.eric.ecommerce.model.Role;
import com.eric.ecommerce.repository.UserRepository;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public void register(RegisterRequest request) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new IllegalArgumentException("E-mail já cadastrado");
		}

		AppUser user = new AppUser();

		user.setNome(request.getNome());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(Role.USER);
		userRepository.save(user);

	}

}

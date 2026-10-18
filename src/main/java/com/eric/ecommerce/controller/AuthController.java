package com.eric.ecommerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eric.ecommerce.dto.auth.LoginRequest;
import com.eric.ecommerce.dto.auth.LoginResponse;
import com.eric.ecommerce.dto.auth.RegisterRequest;
import com.eric.ecommerce.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {

		authService.register(request);

		return ResponseEntity.status(HttpStatus.CREATED).build();

	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

		LoginResponse response = authService.login(request);

		return ResponseEntity.ok(response);

	}

}

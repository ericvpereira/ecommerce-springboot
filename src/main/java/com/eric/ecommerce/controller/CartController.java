package com.eric.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eric.ecommerce.dto.AddCartItemRequest;
import com.eric.ecommerce.service.CartService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	public ResponseEntity<Void> addItem(Authentication authentication, @RequestBody @Valid AddCartItemRequest request) {

		String email = authentication.getName();

		cartService.addProduct(email, request.getProductId(), request.getQuantidade());

		return ResponseEntity.ok().build();
	}

}

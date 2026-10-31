package com.eric.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eric.ecommerce.dto.AddCartItemRequest;
import com.eric.ecommerce.dto.CartResponse;
import com.eric.ecommerce.model.Cart;
import com.eric.ecommerce.service.CartService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
@SecurityRequirement(name = "bearerAuth")
public class CartController {

	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@PostMapping("/items")
	public ResponseEntity<CartResponse> addItem(Authentication authentication,
			@RequestBody @Valid AddCartItemRequest request) {

		String email = authentication.getName();

		Cart cart = cartService.addProduct(email, request.getProductId(), request.getQuantidade());

		CartResponse response = cartService.toResponse(cart);

		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<CartResponse> getCart(Authentication authentication) {

		String email = authentication.getName();

		Cart cart = cartService.getOrCreateCart(email);

		CartResponse response = cartService.toResponse(cart);

		return ResponseEntity.ok(response);

	}
}
